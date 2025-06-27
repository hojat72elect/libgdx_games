package com.riiablo.map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import net.mostlyoriginal.api.event.common.EventSystem;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

import com.riiablo.COFs;
import com.riiablo.Colors;
import com.riiablo.Files;
import com.riiablo.Fonts;
import com.riiablo.Palettes;
import com.riiablo.Riiablo;
import com.riiablo.Textures;
import com.riiablo.codec.COF;
import com.riiablo.codec.D2;
import com.riiablo.codec.DC6;
import com.riiablo.codec.DCC;
import com.riiablo.codec.FontTBL;
import com.riiablo.codec.Palette;
import com.riiablo.codec.StringTBLs;
import com.riiablo.engine.client.AnimationStepper;
import com.riiablo.engine.client.ClientEntityFactory;
import com.riiablo.engine.client.CofAlphaHandler;
import com.riiablo.engine.client.CofLayerCacher;
import com.riiablo.engine.client.CofLayerLoader;
import com.riiablo.engine.client.CofLayerUnloader;
import com.riiablo.engine.client.CofLoader;
import com.riiablo.engine.client.CofResolver;
import com.riiablo.engine.client.CofTransformHandler;
import com.riiablo.engine.client.CofUnloader;
import com.riiablo.engine.client.DialogManager;
import com.riiablo.engine.client.DirectionResolver;
import com.riiablo.engine.client.HoveredManager;
import com.riiablo.engine.client.LabelManager;
import com.riiablo.engine.client.MenuManager;
import com.riiablo.engine.client.MonsterLabelManager;
import com.riiablo.engine.client.SelectableManager;
import com.riiablo.engine.client.WarpSubstManager;
import com.riiablo.engine.client.debug.Box2DDebugger;
import com.riiablo.engine.client.debug.PathDebugger;
import com.riiablo.engine.client.debug.PathfindDebugger;
import com.riiablo.engine.client.debug.RenderSystemDebugger;
import com.riiablo.engine.server.Actioneer;
import com.riiablo.engine.server.AnimDataResolver;
import com.riiablo.engine.server.AnimStepper;
import com.riiablo.engine.server.CofManager;
import com.riiablo.engine.server.ItemInteractor;
import com.riiablo.engine.server.ItemManager;
import com.riiablo.engine.server.ObjectInitializer;
import com.riiablo.engine.server.ObjectInteractor;
import com.riiablo.engine.server.Pathfinder;
import com.riiablo.engine.server.WarpInteractor;
import com.riiablo.engine.server.component.Class;
import com.riiablo.engine.server.component.Classname;
import com.riiablo.engine.server.component.Position;
import com.riiablo.graphics.BlendMode;
import com.riiablo.graphics.PaletteIndexedBatch;
import com.riiablo.item.ItemGenerator;
import com.riiablo.item.VendorGenerator;
import com.riiablo.loader.BitmapFontLoader;
import com.riiablo.loader.COFLoader;
import com.riiablo.loader.DC6Loader;
import com.riiablo.loader.DCCLoader;
import com.riiablo.loader.PaletteLoader;
import com.riiablo.logger.Level;
import com.riiablo.logger.LogManager;
import com.riiablo.logger.Logger;
import com.riiablo.map.DT1.Tile;
import com.riiablo.map.pfa.GraphPath;
import com.riiablo.mpq.MPQFileHandleResolver;
import com.riiablo.tool.LwjglTool;
import com.riiablo.tool.Tool;
import com.riiablo.util.DebugUtils;
import com.riiablo.util.InstallationFinder;

public class MapViewer extends Tool {
  private static final Logger log = LogManager.getLogger(MapViewer.class);

  public static void main(String[] args) {
    LwjglTool.create(MapViewer.class, "map-viewer", args)
        .title("Map Viewer")
        .size(1280, 720)
        .start();
  }

  ShapeRenderer shapes;
  Map map;

  int ent;
  World engine;
  MapManager mapManager;
  RenderSystem mapRenderer;
  PathfindDebugger pathfindDebugger;
  protected ComponentMapper<Position> mPosition;

  BitmapFont font;

  Box2DPhysics box2DPhysics;

  int x, y;
  final Vector2 tmpVec2a = new Vector2();
  final Vector2 tmpVec2b = new Vector2();

  final Vector2 pathStart = new Vector2();
  final Vector2 pathEnd = new Vector2();
  GraphPath generatedPath = new GraphPath();
  GraphPath smoothedPath = new GraphPath();

  boolean drawCrosshair;
  boolean drawGrid;
  boolean drawFlags;
  boolean drawWalls = true;
  boolean drawRoofs = true;
  boolean drawSpecial = true;
  boolean drawRawPathNodes = true;
  boolean drawBox2D = true;
  final StringBuilder builder = new StringBuilder(256);

  FileHandle home;
  int seed;
  int act;
  int diff;

  @Override
  protected String getHelpHeader() {
    return "View and generate maps.\n" +
        "E.g., {cmd} --seed 0xdeadbeef --act 0 --diff 0";
  }

  @Override
  protected void createCliOptions(Options options) {
    super.createCliOptions(options);

    options.addOption(Option
        .builder("d")
        .longOpt("d2")
        .desc("directory containing D2 MPQ files")
        .hasArg()
        .argName("path")
        .build());

    options.addOption(Option
        .builder("s")
        .longOpt("seed")
        .desc("seed used for generation (hex)")
        .hasArg()
        .argName(int.class.getName())
        .build());

    options.addOption(Option
        .builder("r")
        .longOpt("random")
        .desc("generate a random seed")
        .build());

    options.addOption(Option
        .builder("a")
        .longOpt("act")
        .desc("act to generate [" + Riiablo.ACT1 + ".." + Riiablo.NUM_ACTS + ")")
        .hasArg()
        .argName(int.class.getName())
        .build());

    options.addOption(Option
        .builder("d")
        .longOpt("diff")
        .desc("difficulty to generate [" + Riiablo.NORMAL + ".." + Riiablo.NUM_DIFFS + ")")
        .hasArg()
        .argName(int.class.getName())
        .build());
  }

  @Override
  protected void handleCliOptions(String cmd, Options options, CommandLine cli) throws Exception {
    super.handleCliOptions(cmd, options, cli);

    InstallationFinder finder = InstallationFinder.getInstance();
    home = finder.defaultHomeDir("d2", cli.getOptionValue("d2"));

    if (cli.hasOption("random")) {
      seed = -1;
    } else if (cli.hasOption("seed")) {
      try {
        String seedOptionValue = cli.getOptionValue("seed");
        seedOptionValue = StringUtils.removeStartIgnoreCase(seedOptionValue, "0x");
        seed = (int) Long.parseLong(seedOptionValue, 16);
      } catch (NumberFormatException t) {
        log.warn(ExceptionUtils.getRootCauseMessage(t), t);
        ExceptionUtils.wrapAndThrow(t);
        return;
      }
    } else {
      seed = -1;
    }

    if (cli.hasOption("act")) {
      String strValue = cli.getOptionValue("act");
      if (strValue == null) {
        System.out.println("'act' not specified -- defaulting to " + act);
      }

      act = NumberUtils.toInt(strValue, Riiablo.ACT1);
      if (act < Riiablo.ACT1 || act >= Riiablo.NUM_ACTS) {
        System.err.println("Invalid option 'act': " + strValue);
        System.err.println("'act' must be in range [" + Riiablo.ACT1 + ".." + Riiablo.NUM_ACTS + ")");
        System.exit(0);
      }
    } else {
      act = Riiablo.ACT1;
    }

    if (cli.hasOption("diff")) {
      String strValue = cli.getOptionValue("diff");
      if (strValue == null) {
        System.out.println("'diff' not specified -- defaulting to " + diff);
      }

      diff = NumberUtils.toInt(strValue, Riiablo.NORMAL);
      if (diff < Riiablo.NORMAL || diff >= Riiablo.NUM_DIFFS) {
        System.err.println("Invalid option 'diff': " + strValue);
        System.err.println("'diff' must be in range [" + Riiablo.NORMAL + ".." + Riiablo.NUM_DIFFS + ")");
        System.exit(0);
      }
    } else {
      diff = Riiablo.NORMAL;
    }
  }

  @Override
  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);
    LogManager.setLevel(MapViewer.class.getName(), Level.DEBUG);

    Riiablo.home = home = Gdx.files.absolute(home.path());
    MPQFileHandleResolver resolver = Riiablo.mpqs = new MPQFileHandleResolver();

    if (seed == -1) {
      log.info("generating random seed...");
      seed = MathUtils.random.nextInt();
    }
    log.infof("seed: %08x", seed);

    AssetManager assets = Riiablo.assets = new AssetManager();
    assets.setLoader(DS1.class, new DS1Loader(resolver));
    assets.setLoader(DT1.class, new DT1Loader(resolver));
    assets.setLoader(COF.class, new COFLoader(resolver));
    assets.setLoader(DCC.class, new DCCLoader(resolver));
    assets.setLoader(DC6.class, new DC6Loader(resolver));
    assets.setLoader(Palette.class, new PaletteLoader(resolver));
    assets.setLoader(FontTBL.BitmapFont.class, new BitmapFontLoader(resolver));

    font = new BitmapFont();

    Riiablo.files = new Files(assets);
    Riiablo.fonts = new Fonts(assets);
    Riiablo.palettes = new Palettes(assets);
    Riiablo.colors = new Colors();
    Riiablo.textures = new Textures();
    Riiablo.string = new StringTBLs(resolver);
    Riiablo.cofs = new COFs(assets);//COFD2.loadFromFile(resolver.resolve("data\\global\\cmncof_a1.d2"));
    Riiablo.anim = D2.loadFromFile(resolver.resolve("data\\global\\eanimdata.d2"));

    ShaderProgram.pedantic = false;
    ShaderProgram shader = Riiablo.shader = new ShaderProgram(
        Gdx.files.internal("shaders/indexpalette3.vert"),
        Gdx.files.internal("shaders/indexpalette3.frag"));
    if (!shader.isCompiled()) {
      throw new GdxRuntimeException("Error compiling shader: " + shader.getLog());
    }

    PaletteIndexedBatch batch = Riiablo.batch = new PaletteIndexedBatch(2048, shader);
    Riiablo.shapes = shapes = new ShapeRenderer();
    Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

//    Riiablo.engine = engine = new Engine();

    map = new Map(seed, diff);

    mapManager = new MapManager();

    RenderSystem.RENDER_DEBUG_SUBTILE = true;
    mapRenderer = new RenderSystem(batch, map);
    mapRenderer.resize();

    WorldConfiguration config = new WorldConfigurationBuilder()
        .with(new EventSystem())
        .with(new TagManager())
        .with(mapManager)
        .with(new ItemManager())
        .with(new ItemGenerator()) // TODO: remove when support for inventory component added
        .with(new VendorGenerator()) // TODO: remove when support for inventory component added
        .with(new CofManager())
        .with(new ObjectInitializer())
        .with(new ObjectInteractor(), new WarpInteractor(), new ItemInteractor())
        .with(new MenuManager(), new DialogManager())

        .with(new Actioneer())

        .with(box2DPhysics = new Box2DPhysics(1 / 60f))
        .with(new Pathfinder())

        .with(new ClientEntityFactory())
        .with(new AnimDataResolver())
        .with(new AnimStepper())
        .with(new CofUnloader(), new CofResolver(), new CofLoader())
        .with(new CofLayerUnloader(), new CofLayerLoader(), new CofLayerCacher())
        .with(new CofAlphaHandler(), new CofTransformHandler())
        .with(new AnimationStepper())

        .with(new SelectableManager())
        .with(new HoveredManager())
        .with(new WarpSubstManager())

        .with(new DirectionResolver())

        .with(mapRenderer)
        .with(new LabelManager())
        .with(new MonsterLabelManager())

        .with(new PathDebugger())
        .with(pathfindDebugger = new PathfindDebugger())
        .with(new Box2DDebugger())
        .with(new RenderSystemDebugger())
        .build()
        .register("iso", mapRenderer.iso)
        .register("map", map)
        .register("batch", Riiablo.batch)
        .register("shapes", Riiablo.shapes)
        .register("stage", null)
        .register("scaledStage", null)
        ;
    Riiablo.engine = engine = new World(config);
    mPosition = engine.getMapper(Position.class);

    engine.inject(map);
    engine.inject(Act1MapBuilder.INSTANCE);

    map.setAct(act);
    map.load();
    map.finishLoading();
    map.generate();
    mapManager.createEntities();

    box2DPhysics.setEnabled(false);
    box2DPhysics.createBodies();

    RenderSystem.RENDER_DEBUG_TILE = true;

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(new InputAdapter() {
      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (button) {
          case Input.Buttons.RIGHT:
            pathStart.setZero();
            pathEnd.setZero();
            generatedPath.clear();
            smoothedPath.clear();
            break;
          case Input.Buttons.LEFT:
            mapRenderer.iso.agg(pathStart.set(screenX, screenY)).unproject().toWorld();
            pathEnd.setZero();
            break;
        }
        return true;
      }

      @Override
      public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!pathStart.isZero()) mapRenderer.iso.agg(pathEnd.set(screenX, screenY)).unproject().toWorld();
        return true;
      }

      @Override
      public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
          case Input.Buttons.LEFT:
            mapRenderer.iso.agg(pathEnd.set(screenX, screenY)).unproject().toWorld();

            final int flags = Tile.FLAG_BLOCK_WALK;
            final int size = 0;
            map.findPath(pathStart, pathEnd, flags, size, generatedPath);
            smoothedPath.clear();
            smoothedPath.nodes.addAll(generatedPath.nodes);
            map.smoothPath(flags, size, smoothedPath);
            System.out.println(generatedPath + "->" + smoothedPath);
            break;
        }
        return true;
      }
    });
    multiplexer.addProcessor(new InputAdapter() {
      private final float ZOOM_AMOUNT = 0.1f;

      @Override
      public boolean scrolled(float amountX, float amountY) {
        if (amountY < 0) {
          mapRenderer.zoom(Math.max(0.20f, mapRenderer.zoom() - ZOOM_AMOUNT));
        } else {
          mapRenderer.zoom(Math.min(2.50f, mapRenderer.zoom() + ZOOM_AMOUNT));
        }
        return true;
      }

      @Override
      public boolean keyDown(int keycode) {
        switch (keycode) {
          case Input.Keys.TAB:
            RenderSystem.RENDER_DEBUG_GRID++;
            if (RenderSystem.RENDER_DEBUG_GRID > RenderSystem.DEBUG_GRID_MODES) {
              RenderSystem.RENDER_DEBUG_GRID = 0;
            }
            return true;
          case Input.Keys.GRAVE:
            if (UIUtils.shift()) {
              RenderSystem.RENDER_DEBUG_MATERIAL++;
              if (RenderSystem.RENDER_DEBUG_MATERIAL > Map.MAX_FLOORS + 1) {
                RenderSystem.RENDER_DEBUG_MATERIAL = 0;
              }
            } else {
              RenderSystem.RENDER_DEBUG_WALKABLE++;
              if (RenderSystem.RENDER_DEBUG_WALKABLE > Map.MAX_LAYERS + 1) {
                RenderSystem.RENDER_DEBUG_WALKABLE = 0;
              }
            }
            return true;
          case Input.Keys.ALT_LEFT:
            mapRenderer.resize();
            return true;
          case Input.Keys.F1:
            drawCrosshair = !drawCrosshair;
            return true;
          case Input.Keys.F2:
            drawGrid = !drawGrid;
            return true;
          case Input.Keys.F3:
            drawFlags = !drawFlags;
            return true;
          case Input.Keys.F4:
            RenderSystem.RENDER_DEBUG_ENTITIES = !RenderSystem.RENDER_DEBUG_ENTITIES;
            return true;
          case Input.Keys.F5:
            drawWalls = !drawWalls;
            return true;
          case Input.Keys.F6:
            drawRoofs = !drawRoofs;
            return true;
          case Input.Keys.F7:
            drawSpecial = !drawSpecial;
            return true;
          case Input.Keys.F8:
            mapRenderer.setEnabled(!mapRenderer.isEnabled());
            return true;
          case Input.Keys.F9:
            RenderSystem.RENDER_DEBUG_CELLS++;
            if (RenderSystem.RENDER_DEBUG_CELLS > Map.MAX_LAYERS + 1) {
              RenderSystem.RENDER_DEBUG_CELLS = 0;
            }
            return true;
          case Input.Keys.F10:
            drawRawPathNodes = !drawRawPathNodes;
            return true;
          case Input.Keys.F11: {
            BaseSystem debugger = engine.getSystem(Box2DDebugger.class);
            debugger.setEnabled(!debugger.isEnabled());
            return true;
          }
          case Input.Keys.F12:
            BaseSystem debugger;
            debugger = engine.getSystem(RenderSystemDebugger.class);
            debugger.setEnabled(!debugger.isEnabled());
            debugger = engine.getSystem(PathDebugger.class);
            debugger.setEnabled(!debugger.isEnabled());
            debugger = pathfindDebugger;
            debugger.setEnabled(!debugger.isEnabled());
            return true;
          case Input.Keys.W:
          case Input.Keys.UP:
            int amount = UIUtils.ctrl() ? 1 : Tile.SUBTILE_SIZE;
            if (UIUtils.shift()) amount *= 8;
            y -= amount;
            mPosition.get(ent).position.set(x, y);
            return true;
          case Input.Keys.S:
          case Input.Keys.DOWN:
            amount = UIUtils.ctrl() ? 1 : Tile.SUBTILE_SIZE;
            if (UIUtils.shift()) amount *= 8;
            y += amount;
            mPosition.get(ent).position.set(x, y);
            return true;
          case Input.Keys.A:
          case Input.Keys.LEFT:
            amount = UIUtils.ctrl() ? 1 : Tile.SUBTILE_SIZE;
            if (UIUtils.shift()) amount *= 8;
            x -= amount;
            mPosition.get(ent).position.set(x, y);
            return true;
          case Input.Keys.D:
          case Input.Keys.RIGHT:
            amount = UIUtils.ctrl() ? 1 : Tile.SUBTILE_SIZE;
            if (UIUtils.shift()) amount *= 8;
            x += amount;
            mPosition.get(ent).position.set(x, y);
            return true;
//          case Input.Keys.UP:
//            Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY() - 1);
//            return true;
//          case Input.Keys.DOWN:
//            Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY() + 1);
//            return true;
//          case Input.Keys.LEFT:
//            Gdx.input.setCursorPosition(Gdx.input.getX() - 1, Gdx.input.getY());
//            return true;
//          case Input.Keys.RIGHT:
//            Gdx.input.setCursorPosition(Gdx.input.getX() + 1, Gdx.input.getY());
//            return true;
          default:
            return false;
        }
      }

//      @Override
//      public boolean mouseMoved(int screenX, int screenY) {
//        //GridPoint2 pt = mapRenderer.toWorldSpace(screenX, screenY);
//        //System.out.println(pt);
//        return true;
//      }
    });
    Gdx.input.setInputProcessor(multiplexer);

    /*
    //Diablo.dt1s = new DT1s();
    map = Map.build(0, 0, 0);
    map.load();

    mapRenderer.setMap(map);
    //mapRenderer.setFactor(1.75f);

    GridPoint2 origin = map.find(Map.ID.TOWN_ENTRY_1);
    if (origin != null) {
      x = origin.x;
      y = origin.y;
      mapRenderer.setPosition(x, y);
    } else {
      x += DT1.Tile.SUBTILE_CENTER.x;
      y += DT1.Tile.SUBTILE_CENTER.y;
      mapRenderer.setPosition(x, y);
    }
    */

    /*
    for (String asset : Diablo.assets.getAssetNames()) {
      Gdx.app.debug(TAG, Diablo.assets.getReferenceCount(asset) + " : " + asset);
    }

    Gdx.app.debug(TAG, "disposing map...");
    map.dispose();

    for (String asset : Diablo.assets.getAssetNames()) {
      Gdx.app.debug(TAG, Diablo.assets.getReferenceCount(asset) + " : " + asset);
    }
    */

    Vector2 origin = map.find(Map.ID.TOWN_ENTRY_1);
    if (origin == null) origin = map.find(Map.ID.TOWN_ENTRY_2);
    if (origin == null) origin = map.find(Map.ID.TP_LOCATION);
    if (origin != null) {
      x = (int) origin.x;
      y = (int) origin.y;
    }

    // den of evil
//    x = 1500;
//    y = 1000;

    ent = createSrc(x, y);
    mapRenderer.setSrc(ent);
    mapRenderer.updatePosition(true);

    for (String asset : Riiablo.assets.getAssetNames()) {
      log.debug("{} : {}", Riiablo.assets.getReferenceCount(asset), asset);
    }

    log.debug("JAVA: {} Bytes; NATIVE: {} Bytes", Gdx.app.getJavaHeap(), Gdx.app.getNativeHeap());
    Riiablo.batch.setGamma(1.2f);
  }

  private int createSrc(float x, float y) {
    int id = engine.create();
    mPosition.create(id).position.set(x, y);
    engine.getMapper(Classname.class).create(id).classname = "null";
    engine.getMapper(Class.class).create(id).type = null;
    return id;
  }

  @Override
  public void render() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    Riiablo.assets.update();
    engine.setDelta(Gdx.graphics.getDeltaTime());
    engine.process();

    mapRenderer.iso.update();

    PaletteIndexedBatch batch = Riiablo.batch;

    if (pathfindDebugger.isEnabled() && !pathStart.isZero() && !pathEnd.isZero()) {
      pathfindDebugger.drawDebugPath(generatedPath, Color.RED, drawRawPathNodes);
      pathfindDebugger.drawDebugPath(smoothedPath, Color.GREEN, true);

      mapRenderer.iso.agg(tmpVec2a.set(pathStart)).toScreen();
      mapRenderer.iso.agg(tmpVec2b.set(pathEnd)).toScreen();
      shapes.begin();
      shapes.set(ShapeRenderer.ShapeType.Line);
      shapes.setColor(Color.WHITE);
      DebugUtils.drawEllipse2(shapes, tmpVec2a.x, tmpVec2a.y, Tile.SUBTILE_WIDTH, Tile.SUBTILE_HEIGHT);
      shapes.set(ShapeRenderer.ShapeType.Filled);
      shapes.setColor(Color.ORANGE);
      shapes.rectLine(tmpVec2a, tmpVec2b, 1);
      shapes.end();
    }

    final int width  = Gdx.graphics.getWidth();
    final int height = Gdx.graphics.getHeight();

    if (drawCrosshair) {
      shapes.identity();
      shapes.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
      shapes.updateMatrices();
      shapes.begin(ShapeRenderer.ShapeType.Line);
      shapes.setColor(Color.GREEN);
      shapes.line(0, height / 2, width, height / 2);
      shapes.line(width / 2, 0, width / 2, height);
      shapes.end();
    }

    batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    batch.begin();
    batch.setShader(null);

    final int textHeight = 16;
    final String SEPARATOR = "  |  ";
    builder.setLength(0);
    String details = builder
        .append("F1 crosshair").append(SEPARATOR)
        .append("F4 objects").append(SEPARATOR)
        .append("F8 graphics").append(SEPARATOR)
        .append("F10 path nodes").append(SEPARATOR)
        .append("F11 box2d").append(SEPARATOR)
        .append("F12 debug")
        .toString();
    batch.setBlendMode(BlendMode.SOLID, Color.BLACK);
    batch.draw(Riiablo.textures.modal, 0, height - textHeight, width, textHeight);
    font.draw(batch, details, 2, height - 2);

    mapRenderer.iso.agg(tmpVec2a.set(Gdx.input.getX(), Gdx.input.getY())).unproject().toWorld().toTile();
    builder.setLength(0);
    String status = builder
        .append(Gdx.graphics.getFramesPerSecond() + " FPS").append('\n')
        .append(Gdx.app.getJavaHeap() / (1 << 20) + " MB").append('\n')
        .append("(" + (int) tmpVec2a.x + ", " + (int) tmpVec2a.y + ")").append('\n')
        .append("(" + x + ", " + y + ")").append('\n')
        .append("Grid (TAB) " + (RenderSystem.RENDER_DEBUG_GRID == 0 ? "OFF" : RenderSystem.RENDER_DEBUG_GRID)).append('\n')
        .append("Walkable (`) " + (RenderSystem.RENDER_DEBUG_WALKABLE == 0 ? "OFF" : RenderSystem.RENDER_DEBUG_WALKABLE)).append('\n')
        .append("Material (~) " + (RenderSystem.RENDER_DEBUG_MATERIAL == 0 ? "OFF" : RenderSystem.RENDER_DEBUG_MATERIAL)).append('\n')
        .append("Cell (F9) " + (RenderSystem.RENDER_DEBUG_CELLS == 0 ? "OFF" : RenderSystem.RENDER_DEBUG_CELLS)).append('\n')
        .append("Zoom " + String.format("%.2f", mapRenderer.zoom()))
        .toString();
    batch.setBlendMode(BlendMode.SOLID, Color.BLACK);
    batch.draw(Riiablo.textures.modal, 0, height - 158 - textHeight, 120, 158);
    font.draw(batch, status, 0, height - textHeight);

    batch.end();
    batch.setShader(Riiablo.shader);
  }

  @Override
  public void dispose() {
    font.dispose();
    shapes.dispose();
    Riiablo.palettes.dispose();
    Riiablo.textures.dispose();
    Riiablo.assets.dispose();
    Riiablo.batch.dispose();
    Riiablo.shader.dispose();
  }
}
