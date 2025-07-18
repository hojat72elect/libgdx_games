package me.vrekt.oasis.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.vrekt.oasis.entity.GameEntity;
import me.vrekt.oasis.entity.player.mp.NetworkPlayer;
import me.vrekt.oasis.save.world.mp.NetworkPlayerSave;

/**
 * Box2d base world
 */
public abstract class Box2dGameWorld {

    private static final float MAX_FRAME_TIME = 0.25f;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 4;
    private static final float STEP_TIME = 1 / 144.0f;

    protected IntMap<NetworkPlayer> players = new IntMap<>();
    protected IntMap<GameEntity> entities = new IntMap<>();

    // ideally store by something else other than string
    // For now, string is fine.
    protected Map<String, NetworkPlayerSave> playerStorage = new HashMap<>();

    protected World world;
    protected Engine engine;

    protected final Vector2 worldOrigin = new Vector2();

    protected float accumulator;

    public Box2dGameWorld(World world, Engine engine) {
        this.world = world;
        this.engine = engine;
    }

    public World boxWorld() {
        return world;
    }

    public Vector2 worldOrigin() {
        return worldOrigin;
    }

    public Map<String, NetworkPlayerSave> playerStorage() {
        return playerStorage;
    }

    /**
     * Check if this world has the player
     *
     * @param entityId player id
     * @return {@code true} if so
     */
    public boolean hasPlayer(int entityId) {
        return players.containsKey(entityId);
    }

    /**
     * Add a network player
     *
     * @param player player
     */
    public void addPlayer(NetworkPlayer player) {
        players.put(player.entityId(), player);
    }

    /**
     * Get a player by ID
     *
     * @param id their ID
     * @return the player
     */
    public NetworkPlayer getPlayer(int id) {
        return players.get(id);
    }

    public Optional<NetworkPlayer> player(int id) {
        return Optional.ofNullable(getPlayer(id));
    }

    /**
     * Spawn a player in this world
     *
     * @param player player
     */
    public void spawnPlayerInWorld(NetworkPlayer player) {
        addPlayer(player);
    }

    /**
     * Remove a player in this world
     *
     * @param entityId ID
     * @param destroy  if the player should be disposed of
     */
    public void removePlayerInWorld(int entityId, boolean destroy) {
        if (hasPlayer(entityId)) {
            final NetworkPlayer player = players().get(entityId);
            players.remove(entityId);
            player.removeFromWorld();

            if (destroy) player.dispose();
        }
    }

    /**
     * Player transferred to an interior, handle that.
     * Temporarily remove them from the players list and destroy their body.
     *
     * @param player the player
     */
    public void removePlayerTemporarily(NetworkPlayer player) {
        players.remove(player.entityId());
        world.destroyBody(player.body());
    }

    public IntMap<NetworkPlayer> players() {
        return players;
    }

    public IntMap<GameEntity> entities() {
        return entities;
    }

    /**
     * Internal ticking
     *
     * @param delta delta
     */
    protected void tick(float delta) {

    }

    /**
     * Update the physics and entity engines
     *
     * @param delta delta
     * @return the capped frame time delta
     */
    public float tickWorldPhysicsSim(float delta) {
        final float capped = Math.min(delta, MAX_FRAME_TIME);

        stepPhysicsSimulation(capped);
        engine.update(capped);

        return capped;
    }

    /**
     * Step box2d world
     *
     * @param delta delta
     */
    public void stepPhysicsSimulation(float delta) {
        accumulator += delta;

        while (accumulator >= STEP_TIME) {
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= STEP_TIME;
        }
    }

    /**
     * Update player position
     *
     * @param entityId id
     * @param x        x
     * @param y        y
     * @param rotation angle/rotation
     */
    public void updatePlayerPositionInWorld(int entityId, float x, float y, int rotation) {
        player(entityId).ifPresent(p -> p.updateNetworkPosition(x, y, rotation));
    }

    /**
     * Update player velocity
     *
     * @param entityId id
     * @param x        x
     * @param y        y
     * @param rotation angle/rotation
     */
    public void updatePlayerVelocityInWorld(int entityId, float x, float y, int rotation) {
        player(entityId).ifPresent(p -> p.updateNetworkVelocity(x, y, rotation));
    }
}
