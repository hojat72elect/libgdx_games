package me.vrekt.oasis.network.connection.server;

import com.badlogic.gdx.math.MathUtils;

import io.netty.channel.Channel;
import me.vrekt.oasis.item.Item;
import me.vrekt.oasis.item.ItemRegistry;
import me.vrekt.oasis.network.IntegratedGameServer;
import me.vrekt.oasis.network.connection.NetworkConnection;
import me.vrekt.oasis.network.game.HostNetworkHandler;
import me.vrekt.oasis.network.server.entity.player.ServerPlayer;
import me.vrekt.oasis.network.server.world.ServerWorld;
import me.vrekt.oasis.network.server.world.obj.ServerBreakableWorldObject;
import me.vrekt.oasis.network.server.world.obj.ServerWorldObject;
import me.vrekt.oasis.utility.logging.ServerLogging;
import me.vrekt.oasis.world.interior.Interior;
import me.vrekt.oasis.world.obj.interaction.WorldInteractionType;
import me.vrekt.shared.packet.client.C2SAnimateObject;
import me.vrekt.shared.packet.client.C2SDestroyWorldObject;
import me.vrekt.shared.packet.client.C2SInteractWithObject;
import me.vrekt.shared.packet.client.C2SKeepAlive;
import me.vrekt.shared.packet.client.C2SPacketAuthenticate;
import me.vrekt.shared.packet.client.C2SPacketDisconnected;
import me.vrekt.shared.packet.client.C2SPacketJoinWorld;
import me.vrekt.shared.packet.client.C2SPacketPing;
import me.vrekt.shared.packet.client.C2SWorldLoaded;
import me.vrekt.shared.packet.client.interior.C2SInteriorLoaded;
import me.vrekt.shared.packet.client.interior.C2STryEnterInteriorWorld;
import me.vrekt.shared.packet.client.player.C2SChatMessage;
import me.vrekt.shared.packet.client.player.C2SPacketPlayerPosition;
import me.vrekt.shared.packet.client.player.C2SPacketPlayerVelocity;
import me.vrekt.shared.packet.server.interior.S2CEnterInteriorWorld;
import me.vrekt.shared.packet.server.interior.S2CPlayerEnteredInterior;
import me.vrekt.shared.packet.server.obj.S2CAnimateObject;
import me.vrekt.shared.packet.server.obj.S2CDestroyWorldObjectResponse;
import me.vrekt.shared.packet.server.obj.S2CInteractWithObjectResponse;
import me.vrekt.shared.packet.server.obj.S2CNetworkRemoveWorldObject;
import me.vrekt.shared.packet.server.player.S2CAuthenticate;
import me.vrekt.shared.packet.server.player.S2CJoinWorld;
import me.vrekt.shared.packet.server.player.S2CPing;
import me.vrekt.shared.packet.server.player.S2CWorldInvalid;
import me.vrekt.shared.protocol.Packets;
import me.vrekt.shared.protocol.ProtocolDefaults;

/**
 * Connection for a server player
 */
public final class PlayerServerConnection extends NetworkConnection {

    private final IntegratedGameServer server;

    private boolean disconnected;
    private ServerPlayer player;

    private boolean joiningWorld;

    public PlayerServerConnection(Channel channel, IntegratedGameServer server) {
        super(channel, false);
        this.server = server;
        this.isConnected = true;

        attachAll();
    }

    private HostNetworkHandler host() {
        return server.handler();
    }

    private void toHost(Runnable action) {
        host().postNetworkUpdate(action);
    }

    /**
     * @return the player
     */
    public ServerPlayer player() {
        return player;
    }

    /**
     * Attach all relevant packet handlers
     */
    private void attachAll() {
        attach(Packets.C2S_AUTHENTICATE, packet -> handleAuthentication((C2SPacketAuthenticate) packet));
        attach(Packets.C2S_PING, packet -> handlePing((C2SPacketPing) packet));
        attach(Packets.C2S_JOIN_WORLD, packet -> handleJoinWorld((C2SPacketJoinWorld) packet));
        attach(Packets.C2S_WORLD_LOADED, packet -> handlePlayerWorldLoaded((C2SWorldLoaded) packet));
        attach(Packets.C2S_DISCONNECTED, packet -> handleDisconnected((C2SPacketDisconnected) packet));
        attach(Packets.C2S_POSITION, packet -> handlePlayerPosition((C2SPacketPlayerPosition) packet));
        attach(Packets.C2S_VELOCITY, packet -> handlePlayerVelocity((C2SPacketPlayerVelocity) packet));
        attach(Packets.C2S_KEEP_ALIVE, packet -> handleKeepAlive((C2SKeepAlive) packet));
        attach(Packets.C2S_CHAT, packet -> handleChatMessage((C2SChatMessage) packet));
        attach(Packets.C2S_PING, packet -> handlePing((C2SPacketPing) packet));
        attach(Packets.C2S_ANIMATE_OBJECT, packet -> handleAnimateWorldObject((C2SAnimateObject) packet));
        attach(Packets.C2S_DESTROY_OBJECT, packet -> handleDestroyWorldObject((C2SDestroyWorldObject) packet));
        attach(Packets.C2S_INTERACT_WITH_OBJECT, packet -> handleInteractWorldObject((C2SInteractWithObject) packet));
        attach(Packets.C2S_TRY_ENTER_INTERIOR, packet -> handleTryEnterInterior((C2STryEnterInteriorWorld) packet));
        attach(Packets.C2S_INTERIOR_LOADED, packet -> handleInteriorLoaded((C2SInteriorLoaded) packet));
    }

    /**
     * @return {@code true} if the player is in a world.
     */
    private boolean isValid() {
        return player != null && player.isInWorld();
    }

    /**
     * Handle authentication to the server
     * TODO: Server authentication
     *
     * @param packet the packet
     */
    private void handleAuthentication(C2SPacketAuthenticate packet) {
        sendImmediately(new S2CAuthenticate(true, ProtocolDefaults.PROTOCOL_NAME, ProtocolDefaults.PROTOCOL_VERSION));
    }

    /**
     * Handle a player disconnect
     *
     * @param packet the packet
     */
    private void handleDisconnected(C2SPacketDisconnected packet) {
        server.disconnectPlayer(player, packet.reason());
    }

    /**
     * Handle ping time
     *
     * @param packet packet
     */
    private void handlePing(C2SPacketPing packet) {
        sendImmediately(new S2CPing(packet.tick()));
    }

    /**
     * Handle keep alive
     *
     * @param packet packet
     */
    private void handleKeepAlive(C2SKeepAlive packet) {
        // TODO:
    }

    /**
     * Handling joining a world.
     *
     * @param packet the packet
     */
    private void handleJoinWorld(C2SPacketJoinWorld packet) {
        // prevent loaded worlds from being joined or if the player is already joining one.
        if (!server.isWorldReady() || (player != null && joiningWorld)) {
            sendImmediately(new S2CWorldInvalid(packet.worldId(), "World is not ready."));
            return;
        }

        final int worldId = packet.worldId();
        final ServerWorld world = server.getWorld(worldId);
        if (world == null) {
            sendImmediately(new S2CWorldInvalid(packet.worldId(), "World not found. worldId=" + worldId));
            return;
        }

        player = new ServerPlayer(this, server);
        final int entityId = server.playerConnected(player);

        player.setName(packet.username());
        player.setWorldIn(world);
        player.setEntityId(entityId);

        sendImmediately(new S2CJoinWorld(worldId, player.entityId(), world.mspt()));

        joiningWorld = true;
        ServerLogging.info(this, "Player: %s connected", player.name());
    }

    /**
     * Handle when the player loads their world.
     *
     * @param packet the packet
     */
    private void handlePlayerWorldLoaded(C2SWorldLoaded packet) {
        if (!joiningWorld) {
            ServerLogging.info(this, "Out of order join packet");
        } else {
            if (player.world().worldId() != packet.worldId()) {
                player.kick("Invalid world loaded ID. id=" + packet.worldId() + ", w=" + player.world().worldId());
            } else {
                joiningWorld = false;
                player.world().spawnPlayerInWorld(player);
                isServerPlayerReady = true;
            }
        }
    }

    /**
     * Update player position in the server
     *
     * @param packet packet
     */
    public void handlePlayerPosition(C2SPacketPlayerPosition packet) {
        if (isValid()) player.updatePosition(packet.x(), packet.y(), packet.rotation());
    }

    /**
     * Update player velocity in the server
     *
     * @param packet packet
     */
    public void handlePlayerVelocity(C2SPacketPlayerVelocity packet) {
        if (isValid()) player.updateVelocity(packet.x(), packet.y(), packet.rotation());
    }

    /**
     * Handle chat message
     *
     * @param packet packet
     */
    public void handleChatMessage(C2SChatMessage packet) {
        if (packet.message() == null || packet.message().length() > 150 || !isValid()) {
            ServerLogging.warn(this, "Invalid chat message was sent");
        }

        // TODO: EM-178 to host
        // player.world().broadcastImmediatelyExcluded(packet.from(), new S2CChatMessage(packet.from(), packet.message()));
    }

    /**
     * Handle player interact with an object, ensure its valid.
     *
     * @param packet object
     */
    private void handleAnimateWorldObject(C2SAnimateObject packet) {
        if (isValid()) {
            final ServerWorldObject worldObject = player.world().getWorldObject(packet.objectId());
            if (worldObject != null) {
                // broadcasts the relevant packets, also notify host.
                if (worldObject.interact(player)) {
                    toHost(() -> host().handleObjectAnimation(player, worldObject));
                    player.world().broadcastImmediatelyExcluded(player.entityId(), new S2CAnimateObject(worldObject.objectId()));
                }
            }
        }
    }

    /**
     * Handle destroying an object
     * This is not decided by the client, must be validated here
     *
     * @param packet packet
     */
    private void handleDestroyWorldObject(C2SDestroyWorldObject packet) {
        if (isValid()) {
            final ServerWorldObject worldObject = player.world().getWorldObject(packet.objectId());

            // ensure object was touched and we are the same player.
            if (worldObject != null
                    && worldObject.wasInteracted()
                    && worldObject.interactedId() == player.entityId()
                    && worldObject.hasInteractionTimeElapsed()) {

                // player can break this object.
                player.getConnection().sendImmediately(new S2CDestroyWorldObjectResponse(worldObject.objectId(), true));
                // notify others too.
                host().postNetworkUpdate(() -> host().handleObjectDestroyed(player, worldObject));
                // TODO EM-179
                worldObject.playerDestroyed(player);

                // this object will generate a random item to drop
                if (worldObject.type() == WorldInteractionType.BREAKABLE_OBJECT) {
                    generateRandomItem(worldObject.asBreakable());
                }
            } else {
                player.getConnection().sendImmediately(new S2CDestroyWorldObjectResponse(packet.objectId(), false));
            }
        }
    }

    /**
     * Generate a random item and spawn it in
     * Host will see first, a bit unfair but hey lets all be nice!
     *
     * @param object object
     */
    private void generateRandomItem(ServerBreakableWorldObject object) {
        final float unlucky = 0.1f;
        final float multipleItemsChance = 0.2f;

        final boolean isUnlucky = MathUtils.randomBoolean(unlucky);
        if (isUnlucky) return;

        final int amount = MathUtils.randomBoolean(multipleItemsChance) ? MathUtils.random(1, 3) : 1;
        final Item item = ItemRegistry.createRandomItemWithRarity(object.assignedRarity(), amount);

        server.handler().createDroppedItemAndBroadcast(item, object.position().add(0.25f, 0.25f));
    }

    /**
     * Handle interacting with a world object
     *
     * @param packet packet
     */
    private void handleInteractWorldObject(C2SInteractWithObject packet) {
        final ServerWorldObject object = player.world().getWorldObject(packet.objectId());
        if (packet.type() == C2SInteractWithObject.InteractionType.PICK_UP) {
            handlePickupInteraction(object, packet.objectId());
        }
    }

    /**
     * Picking up an item
     *
     * @param object          object
     * @param packetNonNullId non null packet ID
     */
    private void handlePickupInteraction(ServerWorldObject object, int packetNonNullId) {
        if (object == null) {
            player.getConnection().sendImmediately(new S2CInteractWithObjectResponse(packetNonNullId, false));
            return;
        }

        if (!object.wasInteracted()) {
            // hopefully prevent other players from picking it up within a small-time frame
            object.markOwnership(player);
            player.getConnection().sendImmediately(new S2CInteractWithObjectResponse(object.objectId(), true));

            // bye bye
            server.handler().handleObjectDestroyed(player, object);
            player.world().broadcastImmediatelyExcluded(player.entityId(), new S2CNetworkRemoveWorldObject(object.objectId()));
        } else {
            // nope
            player.getConnection().sendImmediately(new S2CInteractWithObjectResponse(object.objectId(), false));
        }
    }

    /**
     * Player wants to enter an interior, delegate to host
     *
     * @param packet packet
     */
    private void handleTryEnterInterior(C2STryEnterInteriorWorld packet) {
        toHost(() -> host().validateAndLoadPlayerEnteredInterior(player, packet.type()));
    }

    /**
     * Will handle additional tasks after the host player has the interior loaded.
     * TODO: Do not access from main game thread - host
     *
     * @param interior interior
     * @param result   result
     */
    public void handleTryInteriorRequestResult(Interior interior, ServerWorld world, boolean result) {
        if (result) {
            player.world().broadcastImmediatelyExcluded(player.entityId(), new S2CPlayerEnteredInterior(interior, player.entityId()));

            player.setAllowedToEnter(interior);
            sendImmediately(new S2CEnterInteriorWorld(interior, true));
            player.transfer(world);
        }
    }

    /**
     * Player finished loading an interior
     *
     * @param packet packet
     */
    private void handleInteriorLoaded(C2SInteriorLoaded packet) {
        // kick the player if they were not given permission
        if (player.allowedToEnter() != packet.type()) {
            player.kick("Cannot enter this interior");
        } else {
            toHost(() -> server.handler().handlePlayerInteriorLoaded(player, packet.type()));
        }
    }

    @Override
    public void channelClosed(Throwable ifAny) {
        if (ifAny != null) ServerLogging.exceptionThrown(this, "Connection closed with exception", ifAny);
        if (isValid()) {
            if (!disconnected)
                server.disconnectPlayer(player, ifAny == null ? null : ifAny.getLocalizedMessage());
        } else {
            if (!disconnected) disconnect();
        }
    }

    @Override
    public void disconnect() {
        if (disconnected) return;
        this.disconnected = true;

        if (channel.isOpen()) channel.close();
    }
}
