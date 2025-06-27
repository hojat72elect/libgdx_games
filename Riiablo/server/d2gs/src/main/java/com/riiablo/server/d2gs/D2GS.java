package com.riiablo.server.d2gs;

import com.google.flatbuffers.FlatBufferBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.BitVector;
import net.mostlyoriginal.api.event.common.EventSystem;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.TimeUtils;

import com.riiablo.COFs;
import com.riiablo.Files;
import com.riiablo.Riiablo;
import com.riiablo.audio.ServerAudio;
import com.riiablo.codec.Animation;
import com.riiablo.codec.D2;
import com.riiablo.codec.StringTBLs;
import com.riiablo.engine.Engine;
import com.riiablo.engine.EntityFactory;
import com.riiablo.engine.server.AIStepper;
import com.riiablo.engine.server.Actioneer;
import com.riiablo.engine.server.AnimDataResolver;
import com.riiablo.engine.server.CofManager;
import com.riiablo.engine.server.ItemInteractor;
import com.riiablo.engine.server.ItemManager;
import com.riiablo.engine.server.ObjectInitializer;
import com.riiablo.engine.server.ObjectInteractor;
import com.riiablo.engine.server.Pathfinder;
import com.riiablo.engine.server.SerializationManager;
import com.riiablo.engine.server.ServerEntityFactory;
import com.riiablo.engine.server.ServerItemManager;
import com.riiablo.engine.server.ServerNetworkIdManager;
import com.riiablo.engine.server.VelocityAdder;
import com.riiablo.engine.server.WarpInteractor;
import com.riiablo.engine.server.component.Networked;
import com.riiablo.item.ItemGenerator;
import com.riiablo.item.VendorGenerator;
import com.riiablo.map.Act1MapBuilder;
import com.riiablo.map.DS1;
import com.riiablo.map.DS1Loader;
import com.riiablo.map.DT1;
import com.riiablo.map.DT1Loader;
import com.riiablo.map.Map;
import com.riiablo.map.MapManager;
import com.riiablo.mpq.MPQFileHandleResolver;
import com.riiablo.net.packet.d2gs.BeltToCursor;
import com.riiablo.net.packet.d2gs.BodyToCursor;
import com.riiablo.net.packet.d2gs.Connection;
import com.riiablo.net.packet.d2gs.CursorToBelt;
import com.riiablo.net.packet.d2gs.CursorToBody;
import com.riiablo.net.packet.d2gs.CursorToGround;
import com.riiablo.net.packet.d2gs.CursorToStore;
import com.riiablo.net.packet.d2gs.D2GSData;
import com.riiablo.net.packet.d2gs.Disconnect;
import com.riiablo.net.packet.d2gs.GroundToCursor;
import com.riiablo.net.packet.d2gs.Ping;
import com.riiablo.net.packet.d2gs.StoreToCursor;
import com.riiablo.net.packet.d2gs.SwapBeltItem;
import com.riiablo.net.packet.d2gs.SwapBodyItem;
import com.riiablo.net.packet.d2gs.SwapStoreItem;
import com.riiablo.save.CharData;
import com.riiablo.util.DebugUtils;

public class D2GS extends ApplicationAdapter {
  private static final String TAG = "D2GS";

  private static final boolean DEBUG                  = true;
  private static final boolean DEBUG_RECEIVED_CACHE   = DEBUG && !true;
  private static final boolean DEBUG_RECEIVED_PACKETS = DEBUG && true;
  private static final boolean DEBUG_SENT_PACKETS     = DEBUG && true;

  private static final int PORT = 6114;
  private static final int MAX_CLIENTS = Riiablo.MAX_PLAYERS;

  public static void main(String[] args) {
    Options options = new Options()
        .addOption("home", true, "directory containing D2 MPQ files")
        .addOption("seed", true, "seed used to generate map")
        .addOption("diff", true, "difficulty (0-2)");

    CommandLine cmd = null;
    try {
      CommandLineParser parser = new DefaultParser();
      cmd = parser.parse(options, args);
    } catch (Throwable t) {
      System.err.println(t.getMessage());
      System.out.println("Failed to start server instance!");
      return;
    }

    FileHandle home = null;
    if (cmd != null && cmd.hasOption("home")) {
      home = new FileHandle(cmd.getOptionValue("home"));
      if (!home.child("d2data.mpq").exists()) {
        throw new GdxRuntimeException("home does not refer to a valid D2 installation");
      }
    } else {
      home = new FileHandle(System.getProperty("user.home")).child("diablo");
      System.out.println("Home not specified, using " + home);
      if (!home.exists() || !home.child("d2data.mpq").exists()) {
        throw new GdxRuntimeException("home does not refer to a valid D2 installation");
      }
    }

    int seed = 0;
    if (cmd.hasOption("seed")) {
      String seedArg = cmd.getOptionValue("seed");
      try {
        seed = Integer.parseInt(seedArg);
      } catch (Throwable t) {
        System.err.println("Invalid seed provided: " + seedArg);
      }
    }

    int diff = 0;
    if (cmd.hasOption("diff")) {
      String diffArg = cmd.getOptionValue("diff");
      try {
        diff = Integer.parseInt(diffArg);
      } catch (Throwable t) {
        System.err.println("Invalid diff provided: " + diffArg);
      }
    }

    HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
    config.updatesPerSecond = (int) Animation.FRAMES_PER_SECOND;
    new HeadlessApplication(new D2GS(home, seed, diff), config);
  }

  ServerSocket server;
  Thread connectionListener;
  volatile boolean kill = false;
  ThreadGroup clientThreads;
  final Client[] clients = new Client[MAX_CLIENTS];
  int numClients = 0;
  int connected = 0;

  final BlockingQueue<Packet> packets = new ArrayBlockingQueue<>(32);
  final Collection<Packet> cache = new ArrayList<>(1024);
  final BlockingQueue<Packet> outPackets = new ArrayBlockingQueue<>(1024);
  final IntIntMap player = new IntIntMap();

  static final BitVector ignoredPackets = new BitVector(D2GSData.names.length); {
    ignoredPackets.set(D2GSData.EntitySync);
  }

  FileHandle home;
  int seed;
  int diff;

  World world;
  Map map;

  EntityFactory factory;
  ItemManager itemManager;
  MapManager mapManager;
  NetworkSynchronizer sync;

  protected ComponentMapper<Networked> mNetworked;

  D2GS(FileHandle home, int seed, int diff) {
    this.home = home;
    this.seed = seed;
    this.diff = diff;
  }

  @Override
  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    final Calendar calendar = Calendar.getInstance();
    DateFormat format = DateFormat.getDateTimeInstance();
    Gdx.app.log(TAG, format.format(calendar.getTime()));

    try {
      InetAddress address = InetAddress.getLocalHost();
      Gdx.app.log(TAG, "IP Address: " + address.getHostAddress() + ":" + PORT);
      Gdx.app.log(TAG, "Host Name: " + address.getHostName());
    } catch (UnknownHostException e) {
      Gdx.app.error(TAG, e.getMessage(), e);
    }

    Riiablo.home = home = Gdx.files.absolute(home.path());
    if (!home.exists() || !home.child("d2data.mpq").exists()) {
      throw new GdxRuntimeException("home does not refer to a valid D2 installation. Copy MPQs to " + home);
    }

    Riiablo.mpqs = new MPQFileHandleResolver();
    Riiablo.assets = new AssetManager();
    Riiablo.files = new Files(Riiablo.assets);
    Riiablo.cofs = new COFs(Riiablo.assets); // TODO: not needed in prod
    Riiablo.string = new StringTBLs(Riiablo.mpqs); // TODO: not needed in prod
    Riiablo.anim = D2.loadFromFile(Riiablo.mpqs.resolve("data\\global\\eanimdata.d2"));
    Riiablo.audio = new ServerAudio(Riiablo.assets);

    // set DT1 to headless mode
    DT1.loadData = false;
    Riiablo.assets.setLoader(DS1.class, new DS1Loader(Riiablo.mpqs));
    Riiablo.assets.setLoader(DT1.class, new DT1Loader(Riiablo.mpqs));

    if (seed == 0) {
      Gdx.app.log(TAG, "Generating seed...");
      seed = 0;
      Gdx.app.log(TAG, "seed=" + seed);
    }

    Gdx.app.log(TAG, "Generating map...");
    map = new Map(seed, diff);
    mapManager = new MapManager();
    Gdx.app.log(TAG, "  generating act 1...");
    long start = TimeUtils.millis();
    map.generate(0);
    Gdx.app.log(TAG, "  act 1 generated in " + (TimeUtils.millis() - start) + "ms");

    Gdx.app.log(TAG, "Loading act 1...");
    map.load();
    map.finishLoading();

    factory = new ServerEntityFactory();
    itemManager = new ServerItemManager();
    mapManager = new MapManager();
    sync = new NetworkSynchronizer();
    WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
        .with(new EventSystem())
        .with(new ServerNetworkIdManager())
        .with(new SerializationManager())
        .with(mapManager)
        .with(itemManager)
        .with(new ItemGenerator())
        .with(new CofManager())
        .with(new ObjectInitializer())
        .with(new ObjectInteractor(), new WarpInteractor(), new ItemInteractor())

        .with(new Actioneer())

        .with(new VendorGenerator())
        .with(new AIStepper())
        .with(new Pathfinder())

        .with(new VelocityAdder()) // FIXME: temp until proper physics implemented

        .with(factory)
        .with(sync)
        .with(new AnimDataResolver())
        ;
    WorldConfiguration config = builder.build()
        .register("map", map)
        .register("factory", factory)
        .register("player", player)
        .register("outPackets", outPackets)
        ;
    Riiablo.engine = world = new World(config);

    world.inject(map);
    world.inject(Act1MapBuilder.INSTANCE);

    map.generate();
    mapManager.createEntities();

    mNetworked = world.getMapper(Networked.class);
    world.delta = Animation.FRAME_DURATION;

    clientThreads = new ThreadGroup("D2GSClients");

    Gdx.app.log(TAG, "Starting server...");
    server = Gdx.net.newServerSocket(Net.Protocol.TCP, PORT, null);
    connectionListener = new Thread(new Runnable() {
      @Override
      public void run() {
        while (!kill) {
          Gdx.app.log(TAG, "waiting...");
          Socket socket = server.accept(null);
          Gdx.app.log(TAG, "connection from " + socket.getRemoteAddress());
          if (numClients >= MAX_CLIENTS) {
            // TODO: send server is full message
            socket.dispose();
          } else {
            try {
              synchronized (clients) {
                int id = ArrayUtils.indexOf(clients, null);
                assert id != ArrayUtils.INDEX_NOT_FOUND : "numClients=" + numClients + " but no index available";
                Gdx.app.log(TAG, "assigned " + socket.getRemoteAddress() + " to " + id);
                Client client = clients[id] = new Client(id, socket);
                numClients++;
                //connected |= (1 << id);
                client.start();
              }
            } catch (Throwable t) {
              Gdx.app.error(TAG, t.getMessage(), t);
              socket.dispose();
            }
          }
        }

        Gdx.app.log(TAG, "killing child threads...");
        synchronized (clients) {
          for (Client client : clients) {
            if (client != null) {
              client.kill = true;
              client.socket.dispose();
              try {
                client.join();
              } catch (Throwable ignored) {}
            }
          }
          numClients = 0;
          connected = 0;
        }

        Gdx.app.log(TAG, "killing thread...");
      }
    });
    connectionListener.setName("D2GS Connection Listener");
    connectionListener.start();
  }

  @Override
  public void dispose() {
    Gdx.app.log(TAG, "Shutting down...");
    kill = true;
    server.dispose();
    try {
      connectionListener.join();
    } catch (Throwable ignored) {}
    Riiablo.assets.dispose();
  }

  @Override
  public void render() {
    cache.clear();
    int cached = packets.drainTo(cache);
    if (DEBUG_RECEIVED_CACHE && cached > 0) Gdx.app.log(TAG, "processing " + cached + " packets");
    for (Packet packet : cache) {
      if (DEBUG_RECEIVED_PACKETS && !ignoredPackets.get(packet.data.dataType())) Gdx.app.log(TAG, "processing " + D2GSData.name(packet.data.dataType()) + " packet from " + packet.id);
      process(packet);
    }

    world.process();

    cache.clear();
    outPackets.drainTo(cache);
    for (Packet packet : cache) {
      if (DEBUG_SENT_PACKETS && !ignoredPackets.get(packet.data.dataType())) Gdx.app.log(TAG, "dispatching " + D2GSData.name(packet.data.dataType()) + " packet to " + String.format("0x%08X", packet.id));
      for (int i = 0, flag = 1; i < MAX_CLIENTS; i++, flag <<= 1) {
        if ((packet.id & flag) == flag && ((connected & flag) == flag || packet.data.dataType() == D2GSData.Connection)) {
          Client client = clients[i];
          if (client == null) continue;
          try {
            if (DEBUG_SENT_PACKETS && !ignoredPackets.get(packet.data.dataType())) Gdx.app.log(TAG, "  dispatching packet to " + i);
            client.send(packet);
          } catch (Throwable t) {
            Gdx.app.error(TAG, t.getMessage(), t);
          }
        }
      }
    }
  }

  private void process(Packet packet) {
    switch (packet.data.dataType()) {
      case D2GSData.Connection:
        Connection(packet);
        break;
      case D2GSData.EntitySync:
        Synchronize(packet);
        break;
      case D2GSData.GroundToCursor:
        GroundToCursor(packet);
        break;
      case D2GSData.CursorToGround:
        CursorToGround(packet);
        break;
      case D2GSData.StoreToCursor:
        StoreToCursor(packet);
        break;
      case D2GSData.CursorToStore:
        CursorToStore(packet);
        break;
      case D2GSData.SwapStoreItem:
        SwapStoreItem(packet);
        break;
      case D2GSData.BodyToCursor:
        BodyToCursor(packet);
        break;
      case D2GSData.CursorToBody:
        CursorToBody(packet);
        break;
      case D2GSData.SwapBodyItem:
        SwapBodyItem(packet);
        break;
      case D2GSData.BeltToCursor:
        BeltToCursor(packet);
        break;
      case D2GSData.CursorToBelt:
        CursorToBelt(packet);
        break;
      case D2GSData.SwapBeltItem:
        SwapBeltItem(packet);
        break;
      case D2GSData.Ping:
        Ping(packet);
        break;
      default:
        Gdx.app.error(TAG, "Unknown packet type: " + packet.data.dataType());
    }
  }

  private void Connection(Packet packet) {
    Connection connection = (Connection) packet.data.data(new Connection());
    String charName = connection.charName();
    int charClass = connection.charClass();
    Gdx.app.log(TAG, "Connection from " + clients[packet.id].socket.getRemoteAddress() + " : " + charName);

    byte[] cofComponents = new byte[16];
    connection.cofComponentsAsByteBuffer().get(cofComponents);
    Gdx.app.log(TAG, "  " + DebugUtils.toByteArray(cofComponents));

    byte[] cofAlphas = new byte[16];
    connection.cofAlphasAsByteBuffer().get(cofAlphas);
    Gdx.app.log(TAG, "  " + Arrays.toString(cofAlphas));
    Gdx.app.log(TAG, "  >" + Arrays.toString(com.riiablo.util.ArrayUtils.toFloatingPoint(cofAlphas)));

    byte[] cofTransforms = new byte[16];
    connection.cofTransformsAsByteBuffer().get(cofTransforms);
    Gdx.app.log(TAG, "  " + DebugUtils.toByteArray(cofTransforms));

    ByteBuffer d2sData = connection.d2sAsByteBuffer();
    CharData charData = CharData.loadFromBuffer(diff, d2sData);
    Gdx.app.log(TAG, "  " + charData);

    Vector2 origin = map.find(Map.ID.TOWN_ENTRY_1);
    if (origin == null) origin = map.find(Map.ID.TOWN_ENTRY_2);
    if (origin == null) origin = map.find(Map.ID.TP_LOCATION);
    int entityId = factory.createPlayer(charData, origin);
    player.put(packet.id, entityId);
    Gdx.app.log(TAG, "  entityId=" + entityId);

    FlatBufferBuilder builder = new FlatBufferBuilder();
    Connection.startConnection(builder);
    Connection.addEntityId(builder, entityId);
    int connectionOffset = Connection.endConnection(builder);
    int offset = com.riiablo.net.packet.d2gs.D2GS.createD2GS(builder, D2GSData.Connection, connectionOffset);
    com.riiablo.net.packet.d2gs.D2GS.finishSizePrefixedD2GSBuffer(builder, offset);
    Packet response = Packet.obtain(1 << packet.id, builder.dataBuffer());
    outPackets.offer(response);

    Synchronize(packet.id, entityId);

    BroadcastConnect(packet.id, connection, charData, entityId);
  }

  private void Synchronize(int id, int entityId) {

  }

  private void BroadcastConnect(int id, Connection connection, CharData charData, int entityId) {
    FlatBufferBuilder builder = new FlatBufferBuilder();
    int charNameOffset = builder.createString(charData.name);

    byte[] components = new byte[16];
    connection.cofComponentsAsByteBuffer().get(components);
    int componentsOffset = Connection.createCofComponentsVector(builder, components);

    byte[] alphas = new byte[16];
    connection.cofAlphasAsByteBuffer().get(alphas);
    int alphasOffset = Connection.createCofAlphasVector(builder, alphas);

    byte[] transforms = new byte[16];
    connection.cofTransformsAsByteBuffer().get(transforms);
    int transformsOffset = Connection.createCofTransformsVector(builder, transforms);

    Connection.startConnection(builder);
    Connection.addEntityId(builder, entityId);
    Connection.addCharClass(builder, charData.charClass);
    Connection.addCharName(builder, charNameOffset);
    Connection.addCofComponents(builder, componentsOffset);
    Connection.addCofAlphas(builder, alphasOffset);
    Connection.addCofTransforms(builder, transformsOffset);
    int connectionOffset = Connection.endConnection(builder);
    int offset = com.riiablo.net.packet.d2gs.D2GS.createD2GS(builder, D2GSData.Connection, connectionOffset);
    com.riiablo.net.packet.d2gs.D2GS.finishSizePrefixedD2GSBuffer(builder, offset);

    Packet broadcast = Packet.obtain(~(1 << id), builder.dataBuffer());
    boolean success = outPackets.offer(broadcast);
    assert success;
  }

  private void Disconnect(int id) {
    int entityId = player.get(id, Engine.INVALID_ENTITY);
    assert entityId != Engine.INVALID_ENTITY;
    FlatBufferBuilder builder = new FlatBufferBuilder();
    int disconnectOffset = Disconnect.createDisconnect(builder, entityId);
    int offset = com.riiablo.net.packet.d2gs.D2GS.createD2GS(builder, D2GSData.Disconnect, disconnectOffset);
    com.riiablo.net.packet.d2gs.D2GS.finishSizePrefixedD2GSBuffer(builder, offset);
    Packet broadcast = Packet.obtain(~(1 << id), builder.dataBuffer());
    outPackets.offer(broadcast);

    world.delete(entityId);
    player.remove(id, Engine.INVALID_ENTITY);
    synchronized (clients) {
      clients[id] = null;
      numClients--;
      connected &= ~(1 << id);
    }
  }

  private void Ping(Packet packet) {
    Ping ping = (Ping) packet.data.data(new Ping());
    FlatBufferBuilder builder = new FlatBufferBuilder(0);
    int dataOffset = Ping.createPing(builder, ping.tickCount(), ping.sendTime(), TimeUtils.millis() - packet.time, false);
    int root = com.riiablo.net.packet.d2gs.D2GS.createD2GS(builder, D2GSData.Ping, dataOffset);
    com.riiablo.net.packet.d2gs.D2GS.finishSizePrefixedD2GSBuffer(builder, root);
    Packet response = Packet.obtain(1 << packet.id, builder.dataBuffer());
    outPackets.offer(response);
  }

  private void Synchronize(Packet packet) {
    int entityId = player.get(packet.id, Engine.INVALID_ENTITY);
    assert entityId != Engine.INVALID_ENTITY;
    sync.sync(entityId, packet.data);
  }

  private int getPlayerEntityId(Packet packet) {
    int entityId = player.get(packet.id, Engine.INVALID_ENTITY);
    assert entityId != Engine.INVALID_ENTITY;
    return entityId;
  }

  private void GroundToCursor(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    GroundToCursor groundToCursor = (GroundToCursor) packet.data.data(new GroundToCursor());
    itemManager.groundToCursor(entityId, groundToCursor.itemId());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void CursorToGround(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    CursorToGround cursorToGround = (CursorToGround) packet.data.data(new CursorToGround());
    itemManager.cursorToGround(entityId);

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void StoreToCursor(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    StoreToCursor storeToCursor = (StoreToCursor) packet.data.data(new StoreToCursor());
    itemManager.storeToCursor(entityId, storeToCursor.itemId());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void CursorToStore(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    CursorToStore cursorToStore = (CursorToStore) packet.data.data(new CursorToStore());
    itemManager.cursorToStore(entityId, cursorToStore.storeLoc(), cursorToStore.x(), cursorToStore.y());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void SwapStoreItem(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    SwapStoreItem swapStoreItem = (SwapStoreItem) packet.data.data(new SwapStoreItem());
    itemManager.swapStoreItem(entityId, swapStoreItem.itemId(), swapStoreItem.storeLoc(), swapStoreItem.x(), swapStoreItem.y());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void BodyToCursor(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    BodyToCursor bodyToCursor = (BodyToCursor) packet.data.data(new BodyToCursor());
    itemManager.bodyToCursor(entityId, bodyToCursor.bodyLoc(), bodyToCursor.merc());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void CursorToBody(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    CursorToBody cursorToBody = (CursorToBody) packet.data.data(new CursorToBody());
    itemManager.cursorToBody(entityId, cursorToBody.bodyLoc(), cursorToBody.merc());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void SwapBodyItem(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    SwapBodyItem swapBodyItem = (SwapBodyItem) packet.data.data(new SwapBodyItem());
    itemManager.swapBodyItem(entityId, swapBodyItem.bodyLoc(), swapBodyItem.merc());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void BeltToCursor(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    BeltToCursor beltToCursor = (BeltToCursor) packet.data.data(new BeltToCursor());
    itemManager.beltToCursor(entityId, beltToCursor.itemId());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void CursorToBelt(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    CursorToBelt cursorToBelt = (CursorToBelt) packet.data.data(new CursorToBelt());
    itemManager.cursorToBelt(entityId, cursorToBelt.x(), cursorToBelt.y());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  private void SwapBeltItem(Packet packet) {
    int entityId = getPlayerEntityId(packet);
    SwapBeltItem swapBeltItem = (SwapBeltItem) packet.data.data(new SwapBeltItem());
    itemManager.swapBeltItem(entityId, swapBeltItem.itemId());

    packet.id = (1 << packet.id);
    outPackets.offer(packet);
  }

  static String generateClientName() {
    return String.format("Client-%08X", MathUtils.random(1, Integer.MAX_VALUE - 1));
  }

  private class Client extends Thread {
    final String TAG;

    int id;
    Socket socket;
    ByteBuffer buffer = ByteBuffer.allocate(8192);
    volatile boolean kill = false;

    Client(int id, Socket socket) {
      super(clientThreads, generateClientName());
      TAG = D2GS.TAG + "{" + id + "}";
      this.id = id;
      this.socket = socket;
    }

    public void send(Packet packet) throws IOException {
      if (!socket.isConnected()) return;
      WritableByteChannel out = Channels.newChannel(socket.getOutputStream());
      packet.buffer.mark();
      out.write(packet.buffer);
      packet.buffer.reset();
      if ((connected & (1 << id)) == 0 && packet.data.dataType() == D2GSData.Connection) {
        connected |= (1 << id);
      }
    }

    @Override
    public void run() {
      while (!kill) {
        try {
          buffer.clear();
          buffer.mark();
          ReadableByteChannel in = Channels.newChannel(socket.getInputStream());
          if (in.read(buffer) == -1) {
            kill = true;
            break;
          }
          buffer.limit(buffer.position());
          buffer.reset();

          ByteBuffer copy = (ByteBuffer) ByteBuffer.wrap(new byte[buffer.limit()]).put(buffer).rewind();
          Packet packet = Packet.obtain(id, copy);
          if (DEBUG_RECEIVED_PACKETS && !ignoredPackets.get(packet.data.dataType())) Gdx.app.log(TAG, "received " + D2GSData.name(packet.data.dataType()) + " packet from " + socket.getRemoteAddress());
          boolean success = packets.offer(packet, 5, TimeUnit.MILLISECONDS);
          if (!success) {
            Gdx.app.log(TAG, "failed to add to queue -- closing " + socket.getRemoteAddress());
            kill = true;
          } else if (packet.data.dataType() == D2GSData.Ping) {
            try {
              Ping ping = (Ping) packet.data.data(new Ping());
              FlatBufferBuilder builder = new FlatBufferBuilder(0);
              int dataOffset = Ping.createPing(builder, ping.tickCount(), ping.sendTime(), 0, true);
              int root = com.riiablo.net.packet.d2gs.D2GS.createD2GS(builder, D2GSData.Ping, dataOffset);
              com.riiablo.net.packet.d2gs.D2GS.finishSizePrefixedD2GSBuffer(builder, root);
              Packet response = Packet.obtain(1 << packet.id, builder.dataBuffer());
              if (DEBUG_SENT_PACKETS && !ignoredPackets.get(packet.data.dataType())) Gdx.app.log(TAG, "dispatching " + D2GSData.name(packet.data.dataType()) + " ACK packet to " + String.format("0x%08X", packet.id));
              send(response);
            } catch (Throwable t) {
              Gdx.app.log(TAG, t.getMessage(), t);
            }
          }
        } catch (Throwable t) {
          Gdx.app.log(TAG, t.getMessage(), t);
          kill = true;
        }
      }

      Gdx.app.log(TAG, "closing socket to " + socket.getRemoteAddress());
      if (socket != null) socket.dispose();
      Disconnect(id);
    }
  }
}
