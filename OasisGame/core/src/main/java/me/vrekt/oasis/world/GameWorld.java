package me.vrekt.oasis.world;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.vrekt.oasis.GameManager;
import me.vrekt.oasis.OasisGame;
import me.vrekt.oasis.ai.goals.EntityGoal;
import me.vrekt.oasis.asset.game.Asset;
import me.vrekt.oasis.asset.settings.OasisGameSettings;
import me.vrekt.oasis.combat.DamageType;
import me.vrekt.oasis.combat.EntityDamageAnimator;
import me.vrekt.oasis.entity.Entities;
import me.vrekt.oasis.entity.EntityType;
import me.vrekt.oasis.entity.GameEntity;
import me.vrekt.oasis.entity.component.facing.EntityRotation;
import me.vrekt.oasis.entity.enemy.EntityEnemy;
import me.vrekt.oasis.entity.enemy.projectile.ProjectileManager;
import me.vrekt.oasis.entity.enemy.projectile.ProjectileResult;
import me.vrekt.oasis.entity.enemy.projectile.ProjectileType;
import me.vrekt.oasis.entity.interactable.EntityInteractable;
import me.vrekt.oasis.entity.player.mp.NetworkPlayer;
import me.vrekt.oasis.entity.player.sp.PlayerSP;
import me.vrekt.oasis.entity.system.EntityUpdateSystem;
import me.vrekt.oasis.graphics.tiled.MapRenderer;
import me.vrekt.oasis.gui.GuiManager;
import me.vrekt.oasis.gui.GuiType;
import me.vrekt.oasis.gui.input.Cursor;
import me.vrekt.oasis.gui.input.InteractKeyListener;
import me.vrekt.oasis.gui.input.MouseListener;
import me.vrekt.oasis.item.Item;
import me.vrekt.oasis.item.ItemRarity;
import me.vrekt.oasis.item.ItemRegistry;
import me.vrekt.oasis.item.Items;
import me.vrekt.oasis.item.weapons.ItemWeapon;
import me.vrekt.oasis.network.game.world.WorldNetworkRenderer;
import me.vrekt.oasis.network.server.world.obj.ServerWorldObject;
import me.vrekt.oasis.save.world.AbstractWorldSaveState;
import me.vrekt.oasis.utility.collision.BasicEntityCollisionHandler;
import me.vrekt.oasis.utility.collision.CollisionShapeCreator;
import me.vrekt.oasis.utility.logging.GameLogging;
import me.vrekt.oasis.utility.tiled.TiledMapLoader;
import me.vrekt.oasis.world.effects.AreaEffectCloud;
import me.vrekt.oasis.world.interior.Interior;
import me.vrekt.oasis.world.interior.misc.LockDifficulty;
import me.vrekt.oasis.world.obj.AbstractWorldObject;
import me.vrekt.oasis.world.obj.DestroyedObject;
import me.vrekt.oasis.world.obj.SimpleWorldObject;
import me.vrekt.oasis.world.obj.TiledWorldObjectProperties;
import me.vrekt.oasis.world.obj.grove.LootGrove;
import me.vrekt.oasis.world.obj.interaction.InteractionManager;
import me.vrekt.oasis.world.obj.interaction.WorldInteractionType;
import me.vrekt.oasis.world.obj.interaction.impl.AbstractInteractableWorldObject;
import me.vrekt.oasis.world.obj.interaction.impl.container.OpenableContainerInteraction;
import me.vrekt.oasis.world.obj.interaction.impl.items.BreakableObjectInteraction;
import me.vrekt.oasis.world.obj.interaction.impl.items.MapItemInteraction;
import me.vrekt.oasis.world.systems.AreaEffectCloudManager;
import me.vrekt.oasis.world.systems.AreaEffectUpdateSystem;
import me.vrekt.oasis.world.systems.NearbyInteriorUpdateSystem;
import me.vrekt.oasis.world.systems.SystemManager;
import me.vrekt.oasis.world.systems.WorldObjectUpdateSystem;
import me.vrekt.oasis.world.tiled.TileMaterialType;
import me.vrekt.oasis.world.tiled.TiledMapCache;
import me.vrekt.oasis.world.utility.Keybindings;
import me.vrekt.shared.network.state.NetworkState;
import me.vrekt.shared.packet.server.obj.S2CNetworkSpawnWorldDrop;

/**
 * Represents a base world within the game
 */
public abstract class GameWorld extends Box2dGameWorld implements WorldInputAdapter, Disposable {

    protected final OasisGame game;
    protected final PlayerSP player;

    protected TiledMap map;
    protected String worldName, worldMap;
    protected int worldId;

    protected final SpriteBatch batch;
    protected final MapRenderer renderer;
    protected final Vector3 cursorInWorld = new Vector3();
    protected boolean isWorldLoaded, isGameSave;
    protected boolean paused;

    protected GuiManager guiManager;
    protected final WorldNetworkRenderer networkRenderer;

    protected final IntMap<GameEntity> nearbyEntities = new IntMap<>();
    protected final Array<ParticleEffect> effects = new Array<>();
    protected final Bag<GameEntity> specialRenderingEntities = new Bag<>();

    protected final SystemManager systemManager;
    protected AreaEffectUpdateSystem effectUpdateSystem;
    protected AreaEffectCloudManager effectCloudManager;

    // destroyed world objects/entities used for saving
    protected final Array<String> deadEnemiesByKey = new Array<>();
    protected final Bag<DestroyedObject> destroyedWorldObjects = new Bag<>();
    protected final Array<String> lootGroveParents = new Array<>();

    // objects within this world
    protected final Map<String, AbstractWorldObject> worldObjects = new HashMap<>();
    protected final IntMap<AbstractInteractableWorldObject> interactableWorldObjects = new IntMap<>();
    protected final Array<Vector2> paths = new Array<>();

    protected final EnumMap<Interior, GameWorldInterior> interiorWorlds = new EnumMap<>(Interior.class);

    // all mouse listeners
    protected final Array<MouseListener> mouseListeners = new Array<>();
    protected final Array<InteractKeyListener> keyListeners = new Array<>();

    protected final InteractionManager interactionManager;
    // last tick update, 50ms = 1 tick
    protected long lastTick;

    protected final EntityDamageAnimator worldDamageAnimator;
    protected final PerformanceCounter performanceCounter;
    protected WorldSaveLoader saveLoader;

    protected ProjectileManager projectileManager;
    protected ShapeRenderer debugRenderer;

    protected Box2DDebugRenderer boxRenderer;

    // indicates this world should be saved
    protected boolean hasVisited;
    protected boolean flipPlayerCollision;

    protected TiledMapCache mapCache;
    protected boolean isNetworked;

    public GameWorld(OasisGame game, PlayerSP player, World world) {
        super(world, new PooledEngine());

        this.player = player;
        this.game = game;
        this.renderer = game.getRenderer();
        this.batch = game.getBatch();
        this.guiManager = game.guiManager;
        this.interactionManager = new InteractionManager();
        this.networkRenderer = new WorldNetworkRenderer();
        this.projectileManager = new ProjectileManager();
        this.systemManager = new SystemManager();
        this.worldDamageAnimator = new EntityDamageAnimator();
        this.performanceCounter = new PerformanceCounter("GameWorldPerformanceCounter");
    }

    /**
     * @return the name of this world
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * @return the resource location of the Tiled map.
     */
    public String getWorldMap() {
        return worldMap;
    }

    public PlayerSP player() {
        return player;
    }

    public OasisGame getGame() {
        return game;
    }

    public MapRenderer getRenderer() {
        return renderer;
    }

    public boolean isWorldLoaded() {
        return isWorldLoaded;
    }

    /**
     * @return {@code true} if this world is an interior
     */
    public boolean isInterior() {
        return false;
    }

    /**
     * @return the average time it took to tick.
     */
    public float averageTickingTime() {
        return performanceCounter.time.average;
    }

    public int worldId() {
        return worldId;
    }

    /**
     * @return save loader
     */
    public WorldSaveLoader loader() {
        if (saveLoader == null) this.saveLoader = new WorldSaveLoader(this);
        return saveLoader;
    }

    public void setHasVisited(boolean hasVisited) {
        this.hasVisited = hasVisited;
    }

    public boolean hasVisited() {
        return hasVisited;
    }

    /**
     * Check if a player is visible
     *
     * @param player player
     * @return {@code true} if the player is visible and in this world
     */
    public boolean isPlayerVisible(NetworkPlayer player) {
        return players.containsKey(player.entityId()) && player.isInView(getRenderer().getCamera());
    }

    /**
     * Get the correct sound at the tile position
     *
     * @return the sound
     */
    public TileMaterialType getMaterialAt() {
        return mapCache.getMaterialAt((int) player.getPosition().x, (int) player.getPosition().y);
    }

    /**
     * Initialize before loading
     */
    protected void init() {

    }

    /**
     * Invoked after loading is finished
     */
    protected void finalizeWorld() {

    }

    /**
     * Load this world
     * Should be overridden to provide the correct map and scaling
     */
    public void loadWorldTiledMap(boolean isGameSave) {
        this.isGameSave = isGameSave;
    }

    /**
     * Load this world from the network
     */
    public void loadNetworkWorld() {
        this.isNetworked = true;
        this.loadWorldTiledMap(false);
    }

    /**
     * Post load - after loading a save.
     */
    public void postLoad(AbstractWorldSaveState state) {
        // here we can actually check afterward if a loot grove has already generated.
        if (state.lootGroveParents() != null) {
            for (String lootGroveParent : state.lootGroveParents()) {
                lootGroveParents.add(lootGroveParent);
            }
        }
        generateLootGroves(map, game.asset(), OasisGameSettings.SCALE);
    }

    /**
     * Create and initialize this world.
     *
     * @param worldMap   the map of the world
     * @param worldScale the scale of the world
     */
    protected void loadTiledMap(TiledMap worldMap, float worldScale) {
        this.map = worldMap;
        this.mapCache = new TiledMapCache(map);

        debugRenderer = new ShapeRenderer();
        boxRenderer = new Box2DDebugRenderer();

        init();

        world.setContactListener(new BasicEntityCollisionHandler());
        TiledMapLoader.loadMapActions(worldMap, worldScale, worldOrigin, new Rectangle());
        TiledMapLoader.loadMapCollision(worldMap, worldScale, world);
        if (!game.isInMultiplayerGame()) buildEntityPathing(worldMap, worldScale);
        // only create entities if this world is hosted or singleplayer.
        if (!game.isInMultiplayerGame()) createEntities(game.asset(), worldMap, worldScale);
        loadParticleEffects(worldMap, game.asset(), worldScale);

        // only create objects if this world is hosted or singleplayer.
        if (!game.isInMultiplayerGame()) createWorldObjects(worldMap, game.asset(), worldScale);
        createInteriors(worldMap, worldScale);
        if (!game.isInMultiplayerGame()) createEntityGoals(worldMap, worldScale);

        if (!isGameSave) {
            // do not generate yet if we are a save.
            generateLootGroves(worldMap, game.asset(), worldScale);
        }

        addDefaultWorldSystems();
        finalizeWorld();

        isWorldLoaded = true;
        game.setReady(true);
    }

    /**
     * Adds the default world systems
     */
    protected void addDefaultWorldSystems() {
        engine.addSystem(new EntityUpdateSystem(game, this));

        effectCloudManager = new AreaEffectCloudManager();

        systemManager.add(effectUpdateSystem = new AreaEffectUpdateSystem(effectCloudManager));
        if (!isInterior())
            systemManager.add(new NearbyInteriorUpdateSystem(this));

        systemManager.add(new WorldObjectUpdateSystem(this));
    }

    /**
     * Update the tiled renderer to this worlds map.
     */
    protected void updateRendererMap() {
        renderer.setTiledMap(map, worldOrigin.x, worldOrigin.y);
    }

    /**
     * Enter an interior
     *
     * @param interior the interior
     */
    protected void enterInterior(GameWorldInterior interior) {
        player.setEnteringNewWorld(true);
        game.worldManager().transferIn(player, this, interior);
    }

    /**
     * Enter this world or interior
     */
    public void enterWorld() {
        if (!isWorldLoaded) throw new UnsupportedOperationException("World is not loaded.");
        updateEnteringWorldState();
    }

    /**
     * Update the player state + world state
     */
    protected void updateEnteringWorldState() {
        player.updateWorldState(this);
        updateRendererMap();

        if (player.getTransformComponent().position.isZero()) {
            player.getTransformComponent().position.set(worldOrigin);
        }

        player.createCircleBody(world, flipPlayerCollision);
        // cached position of whatever it should be
        player.setPosition(player.getTransformComponent().position);

        game.resetScreen();
        game.worldManager().setActiveWorld(this);
        game.multiplexer().addProcessor(this);

        guiManager.resetCursor();

        if (game.isInMultiplayerGame()) {
            if (isInterior()) {
                player.getConnection().updateInteriorHasLoaded(((GameWorldInterior) this).type);
            } else {
                player.getConnection().updateWorldHasLoaded(worldId);
            }
        }

        player.setEnteringNewWorld(false);
    }

    public void exit() {
        game.multiplexer().removeProcessor(this);
    }

    /**
     * Spawn a projectile in this world
     *
     * @param type   type
     * @param origin origin
     * @param target target
     * @param result the result callback
     */
    public void spawnProjectile(ProjectileType type,
                                Vector2 origin,
                                Vector2 target,
                                ProjectileResult result) {
        projectileManager.spawnProjectile(type, origin, target, result);
    }

    /**
     * Spawn a projectile that has a 'death' animation
     *
     * @param type      type
     * @param animation animation
     * @param origin    origin
     * @param target    target
     * @param result    result
     */
    public void spawnAnimatedProjectile(ProjectileType type,
                                        Animation<TextureRegion> animation,
                                        Vector2 origin,
                                        Vector2 target,
                                        ProjectileResult result) {
        projectileManager.spawnAnimatedProjectile(type, animation, origin, target, result);
    }

    /**
     * Spawn an effect cloud
     * TODO: Better system manager I guess
     *
     * @param effectCloud effect cloud
     */
    public void spawnEffectCloud(AreaEffectCloud effectCloud) {
        effectCloudManager.create(effectCloud);
    }

    /**
     * Check the entities status within an area effect
     *
     * @param entity the entity
     */
    public void checkAreaEffects(GameEntity entity) {
        effectCloudManager.processEntity(entity);
    }

    /**
     * Register damage to be animated
     *
     * @param entity entity
     * @param amount amount
     * @param type   type
     */
    public void registerEntityDamage(GameEntity entity, float amount, DamageType type) {
        worldDamageAnimator.store(entity, amount, type);
    }

    /**
     * Unload the collision within this world
     */
    public void unloadBox2dWorld() {
        final Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (int i = 0; i < bodies.size; i++) {
            if (!world.isLocked())
                world.destroyBody(bodies.get(i));
        }

        world.dispose();
        world = null;
    }

    /**
     * Creates and loads all entities within this world
     *
     * @param asset      asset
     * @param worldMap   map
     * @param worldScale scale
     */
    protected void createEntities(Asset asset, TiledMap worldMap, float worldScale) {
        TiledMapLoader.loadMapObjects(worldMap, worldScale, "Entities", (object, rectangle) -> {
            final String key = TiledMapLoader.ofString(object, "key");
            if (key == null) {
                GameLogging.warn(this, "No key for an entity @ %f,%f", rectangle.x, rectangle.y);
                return;
            }

            final boolean enemy = TiledMapLoader.ofBoolean(object, "enemy");
            final boolean interactable = TiledMapLoader.ofBoolean(object, "interactable");

            if (enemy) {
                createEnemy(key, rectangle, asset);
            } else if (interactable) {
                createInteractableEntity(key, rectangle, asset);
            } else {
                createRegularEntity(key, rectangle, asset);
            }
        });

        GameLogging.info(this, "Loaded %d entities.", entities.size);
    }

    /**
     * Create an enemy
     *
     * @param key       entity key type
     * @param rectangle bounds
     * @param asset     asset
     */
    protected void createEnemy(String key, Rectangle rectangle, Asset asset) {
        final EntityEnemy enemy = Entities.enemy(key, this, new Vector2(rectangle.x, rectangle.y), game);
        enemy.setNetworked(isNetworked);
        enemy.load(asset);
        populateEntity(enemy);

        GameLogging.info(this, "Loaded enemy: %s", enemy.name());
    }

    /**
     * Create an interactable entity
     *
     * @param key       entity key type
     * @param rectangle bounds
     * @param asset     asset
     */
    protected void createInteractableEntity(String key, Rectangle rectangle, Asset asset) {
        final EntityInteractable entity = Entities.interactable(key, this, new Vector2(rectangle.x, rectangle.y), game);
        entity.setNetworked(isNetworked);
        entity.load(asset);
        populateEntity(entity);

        if (entity.renderWithMap()) {
            specialRenderingEntities.add(entity);
        }

        GameLogging.info(this, "Loaded interactable entity: %s", entity.name());
    }

    /**
     * Create a regular entity
     *
     * @param key       entity key type
     * @param rectangle bounds
     * @param asset     asset
     */
    protected void createRegularEntity(String key, Rectangle rectangle, Asset asset) {
        final GameEntity entity = Entities.generic(key, this, new Vector2(rectangle.x, rectangle.y), game);
        entity.setNetworked(isNetworked);
        entity.load(asset);
        populateEntity(entity);

        GameLogging.info(this, "Loaded regular entity: %s", entity.name());
    }

    /**
     * @return get the bag of special entities that render with the map
     */
    public Bag<GameEntity> specialRenderingEntities() {
        return specialRenderingEntities;
    }

    /**
     * Populate this entity to the engine and list
     *
     * @param entity the entity
     */
    protected void populateEntity(GameEntity entity) {
        entity.setEntityId(entities.size + 1);
        entities.put(entity.entityId(), entity);
        engine.addEntity(entity.getEntity());
    }

    /**
     * Add a networked entity
     *
     * @param entity the entity
     */
    public void addNetworkedEntity(GameEntity entity) {
        entities.put(entity.entityId(), entity);
        engine.addEntity(entity.getEntity());

        if (entity.renderWithMap()) {
            specialRenderingEntities.add(entity);
        }
    }

    /**
     * Transfer this entity to an interior
     *
     * @param entity the entity
     * @param other  the interior
     */
    public void transferEntityTo(EntityInteractable entity, GameWorldInterior other) {
        removeEntity(entity, false);
        world.destroyBody(entity.body());

        entity.createRectangleBody(other.world, Vector2.Zero);
        entity.setWorldIn(other);
        other.populateEntity(entity);
    }

    /**
     * Remove a dead entity now.
     * Should be removed from the {@code entities} list from whoever calls the function
     * Usually, via iterator.
     *
     * @param entity entity
     */
    public void removeAndDestroyDeadEntityNow(GameEntity entity) {
        engine.removeEntity(entity.getEntity());
        nearbyEntities.remove(entity.entityId());
        deadEnemiesByKey.add(entity.key());
        entity.dispose();
    }

    /**
     * Remove the entity
     *
     * @param entity  entity
     * @param destroy if {@code true} entity will be disposed of.
     */
    public void removeEntity(GameEntity entity, boolean destroy) {
        engine.removeEntity(entity.getEntity());
        nearbyEntities.remove(entity.entityId());
        entities.remove(entity.entityId());

        if (destroy) entity.dispose();
    }

    /**
     * Queue for a dead entity to be removed from this world.
     * Entity will be removed next tick.
     *
     * @param entity the entity
     */
    public void queueRemoveDeadEntity(GameEntity entity) {
        entity.queueForRemoval();
    }

    /**
     * @return dead enemies by key
     */
    public Array<String> deadEnemies() {
        return deadEnemiesByKey;
    }

    /**
     * Generate rewards within all loot-groves
     *
     * @param worldMap   map
     * @param asset      asset
     * @param worldScale scale
     */
    protected void generateLootGroves(TiledMap worldMap, Asset asset, float worldScale) {
        final Map<String, LootGrove> registry = loadLootGroveParents(worldMap, worldScale);

        TiledMapLoader.loadMapObjects(worldMap, worldScale, "LootGroveChildren", (object, rectangle) -> {
            final String childKey = TiledMapLoader.ofString(object, "child_key");
            final LootGrove grove = registry.get(childKey);
            if (grove != null) {
                final float offsetX = TiledMapLoader.ofFloat(object, "offset_x", 0.0f) * OasisGameSettings.SCALE;
                grove.addRewardPoint(new Vector2(rectangle.x - offsetX, rectangle.y));
            }
        });

        registry.values().forEach(lg -> lg.generate(this, asset));
        GameLogging.info(this, "Generated a total of %d loot-groves", registry.size());
    }

    /**
     * Load the parents/owners of all loot-groves
     *
     * @param worldMap   map
     * @param worldScale scale
     * @return all loot groves
     */
    protected Map<String, LootGrove> loadLootGroveParents(TiledMap worldMap, float worldScale) {
        final Map<String, LootGrove> registry = new HashMap<>();

        TiledMapLoader.loadMapObjects(worldMap, worldScale, "LootGroves", (object, rectangle) -> {
            final String key = TiledMapLoader.ofString(object, "children");
            if (key == null || registry.containsKey(key)) {
                GameLogging.warn(this, "Invalid map data for a loot grove! obj-name=%s, key=%s", object.getName(), key);
            } else {
                // only create a loot grove if it has not been saved.
                if (!lootGroveParents.contains(key, false)) {
                    lootGroveParents.add(key);

                    final String rarity = TiledMapLoader.ofString(object, "rarity");
                    final LootGrove grove = new LootGrove(key, ItemRarity.valueOf(rarity));
                    registry.put(key, grove);

                    GameLogging.info(this, "Found loot-grove %s", key);
                } else {
                    GameLogging.info(this, "Skipping loot-grove %s", key);
                }
            }
        });
        return registry;
    }

    /**
     * @return a list of all loot grove parents
     */
    public Array<String> lootGroveParents() {
        return lootGroveParents;
    }

    /**
     * Load particles
     *
     * @param worldMap   the map of the world
     * @param asset      asset
     * @param worldScale the scale of the world
     */
    protected void loadParticleEffects(TiledMap worldMap, Asset asset, float worldScale) {
        final boolean result = TiledMapLoader.loadMapObjects(worldMap, worldScale, "Particles", (object, rectangle) -> {
            final ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("world/asset/" + object.getName()), asset.getAtlasAssets());
            effect.setPosition(rectangle.x, rectangle.y);
            effect.start();
        });

        if (result) GameLogging.info(this, "Loaded %d particle effects.", effects.size);
    }

    /**
     * Load world objects
     *
     * @param worldMap   the map of the world
     * @param worldScale the scale of the world
     */
    protected void createWorldObjects(TiledMap worldMap, Asset asset, float worldScale) {
        TiledMapLoader.loadMapObjects(worldMap, worldScale, "WorldObjects", (object, rectangle) -> {
            try {
                final TiledWorldObjectProperties properties = new TiledWorldObjectProperties(object);
                if (properties.interactable) {
                    // -1 indicates generate ID for us.
                    createInteractableObject(properties, object, rectangle, worldScale, asset, -1);
                } else {
                    createRegularObject(properties, object, rectangle, worldScale, asset);
                }
            } catch (Exception any) {
                GameLogging.exceptionThrown(this, "Failed to load a world object", any);
            }
        });

        GameLogging.info(this, "Loaded %d interactable objects.", interactableWorldObjects.size);
        GameLogging.info(this, "Loaded %d world objects.", worldObjects.size());
    }

    /**
     * Create an interactable world object
     *
     * @param properties properties
     * @param object     map object
     * @param rectangle  rectangle bounds
     * @param worldScale scale
     * @param asset      asset
     */
    public void createInteractableObject(TiledWorldObjectProperties properties,
                                         MapObject object,
                                         Rectangle rectangle,
                                         float worldScale,
                                         Asset asset,
                                         int id) {
        final boolean isKeyed = properties.key != null;
        final AbstractInteractableWorldObject worldObject = isKeyed
                ? properties.interactionType.getKeyed(properties.key, interactionManager)
                : properties.interactionType.getKeyless(this, object);

        // calculate position based on offsets
        final float positionX = properties.offsetX ? rectangle.x - (properties.sizeX * worldScale) : rectangle.x;
        final float positionY = properties.offsetY ? rectangle.y - (properties.sizeY * worldScale) : rectangle.y;

        worldObject.setWorldIn(this);
        worldObject.setPosition(positionX, positionY);
        worldObject.setSize(rectangle.width, rectangle.height);
        worldObject.setInteractionRange(properties.interactionRange);
        worldObject.setObject(object);

        if (properties.texture != null) {
            worldObject.setTextureAndSize(properties.texture, asset.get(properties.texture));
        }

        worldObject.load(asset);
        createObjectParticles(worldObject, object, asset);

        // assign ID if none
        id = id == -1 ? assignUniqueObjectId(worldObject) : id;
        worldObject.setObjectId(id);

        // load collision for this object
        // In the future: collision body from texture?
        if (properties.hasCollision) createObjectCollisionBody(worldObject, rectangle);
        interactableWorldObjects.put(id, worldObject);

        if (worldObject.isMouseable()) {
            mouseListeners.add(worldObject.mouse());
        } else if (worldObject.isKeyable()) {
            keyListeners.add(worldObject.keys());
        }

        GameLogging.info(this, "Loaded interaction object %s", properties.interactionType);
    }

    /**
     * Assign a unique object ID
     *
     * @param object object
     * @return the unique ID
     */
    protected int assignUniqueObjectId(AbstractInteractableWorldObject object) {
        final int id = (int) (Vector2.len2(object.getPosition().x, object.getPosition().y) + interactableWorldObjects.size + 1);
        object.setObjectId(id);
        return id;
    }

    /**
     * Create a regular world object
     *
     * @param properties properties
     * @param object     map object
     * @param rectangle  rectangle bounds
     * @param worldScale scale
     * @param asset      asset
     */
    private void createRegularObject(TiledWorldObjectProperties properties,
                                     MapObject object,
                                     Rectangle rectangle,
                                     float worldScale,
                                     Asset asset) {
        // load base object
        final AbstractWorldObject worldObject = new SimpleWorldObject(properties.key);

        worldObject.load(asset);
        worldObject.setWorldIn(this);
        worldObject.setObject(object);

        // find texture of this object
        final TextureRegion texture = asset.get(properties.texture);
        final boolean drawable = texture != null;

        final float positionX = properties.offsetX ? (drawable
                ? rectangle.x - (texture.getRegionWidth() * worldScale)
                : rectangle.x - (properties.sizeX * worldScale))
                : rectangle.x;
        final float positionY = properties.offsetY ? (drawable
                ? rectangle.y - (texture.getRegionHeight() * worldScale)
                : rectangle.y - (properties.sizeY * worldScale))
                : rectangle.y;

        if (properties.hasCollision && drawable) {
            createObjectCollisionBodyFromTexture(worldObject, rectangle, texture, positionX, positionY);
        } else if (properties.hasCollision) {
            createObjectCollisionBody(worldObject, rectangle);
        }

        if (drawable) worldObject.setTexture(properties.texture, texture);
        worldObject.setPosition(positionX, positionY);
        if (drawable)
            worldObject.setSize(texture.getRegionWidth() * worldScale, texture.getRegionHeight() * worldScale);

        createObjectParticles(worldObject, object, asset);
        worldObjects.put(properties.key, worldObject);
    }

    /**
     * Spawn a world drop interaction
     *
     * @param item     the item
     * @param position the position
     */
    public void spawnWorldDrop(Item item, Vector2 position) {
        final MapItemInteraction interaction = new MapItemInteraction(this, item, position);

        interaction.load(game.asset());
        interactableWorldObjects.put(assignUniqueObjectId(interaction), interaction);

        // generally will always be a mouseable interaction
        mouseListeners.add(interaction.mouse());

        // broadcast this to other players.
        if (game.isHostingMultiplayerGame()) {
            game.integratedServer().activeWorld().broadcastImmediately(new S2CNetworkSpawnWorldDrop(item, position, interaction.objectId()));
            final ServerWorldObject object = new ServerWorldObject(game.integratedServer().activeWorld(), interaction);
            game.integratedServer().activeWorld().addWorldObject(object);
        }
    }

    /**
     * Spawn a world drop interaction
     * This method should not be used in cases where host multiplayer is enabled.
     *
     * @param item     the item
     * @param position the position
     * @param objectId predefined object ID.
     */
    public void localSpawnWorldDrop(Item item, Vector2 position, int objectId) {
        final MapItemInteraction interaction = new MapItemInteraction(this, item, position);
        interaction.setObjectId(objectId);
        interaction.load(game.asset());
        interactableWorldObjects.put(objectId, interaction);

        // generally will always be a mouseable interaction
        mouseListeners.add(interaction.mouse());
    }

    /**
     * Spawn a world drop interaction
     *
     * @param type     item
     * @param amount   the amount
     * @param position position
     */
    public void localSpawnWorldDrop(Items type, int amount, Vector2 position) {
        final Item item = ItemRegistry.createItem(type, amount);
        final MapItemInteraction interaction = new MapItemInteraction(this, item, position);

        interaction.load(game.asset());
        interactableWorldObjects.put(assignUniqueObjectId(interaction), interaction);

        // generally will always be a mouseable interaction
        mouseListeners.add(interaction.mouse());
    }

    /**
     * Create a world drop but do not spawn it. Used in loading saves.
     *
     * @param type     type
     * @param amount   amount
     * @param position position
     * @return the object
     */
    public MapItemInteraction createWorldDrop(Items type, int amount, Vector2 position) {
        final Item item = ItemRegistry.createItem(type, amount);
        final MapItemInteraction interaction = new MapItemInteraction(this, item, position);
        interaction.load(game.asset());
        return interaction;
    }

    /**
     * Add an interaction
     *
     * @param interaction the interaction
     */
    public void addInteraction(AbstractInteractableWorldObject interaction, int id) {
        interactableWorldObjects.put(id == -1 ? assignUniqueObjectId(interaction) : id, interaction);

        if (interaction.isKeyable()) {
            keyListeners.add(interaction.keys());
        } else if (interaction.isMouseable()) {
            mouseListeners.add(interaction.mouse());
        }
    }

    /**
     * Check if this world has a simple object
     *
     * @param key key
     * @return {@code true} if so
     */
    public boolean hasSimpleObject(String key) {
        return worldObjects.containsKey(key);
    }

    /**
     * Remove an object from the map
     *
     * @param key the key
     */
    public void removeSimpleObject(String key) {
        final SimpleWorldObject object = (SimpleWorldObject) worldObjects.get(key);
        if (object == null) return;

        destroyedWorldObjects.add(new DestroyedObject(key));

        object.destroyCollision();
        object.dispose();

        worldObjects.remove(key);
    }

    /**
     * Remove an object that was saved as destroyed
     *
     * @param object the destroyed object.
     */
    protected void removeDestroyedSaveObject(DestroyedObject object) {
        if (object.type() != null) {
            // find the exact interaction
            for (AbstractInteractableWorldObject worldObject : interactableWorldObjects.values()) {
                if (worldObject.getType() == object.type() && worldObject.getPosition().equals(object.position())) {
                    removeInteraction(worldObject);
                    break;
                }
            }
        } else {
            // otherwise, normal destroy.
            removeSimpleObject(object.key());
        }
    }

    /**
     * Remove an interaction
     *
     * @param object object
     */
    public void removeInteraction(AbstractInteractableWorldObject object) {
        object.destroyCollision();
        object.dispose();

        interactableWorldObjects.remove(object.objectId());

        // just catch all here.
        keyListeners.removeValue(object.keys(), true);
        mouseListeners.removeValue(object.mouse(), true);

        destroyedWorldObjects.add(new DestroyedObject(object.getKey(), object.getType(), object.getPosition()));
    }

    /**
     * Invalid an interaction, don't remove it but reset it and hide it.
     *
     * @param object object
     */
    public void invalidateWorldObject(AbstractInteractableWorldObject object) {
        object.reset();
        object.hide();
    }

    /**
     * Show the world object again
     *
     * @param object object
     */
    public void reinstateWorldObject(AbstractInteractableWorldObject object) {
        object.show();
    }

    /**
     * Get a world object by ID
     *
     * @param id id
     * @return the object or {@code null} if not found.
     */
    public AbstractInteractableWorldObject getWorldObjectById(int id) {
        return interactableWorldObjects.get(id);
    }

    /**
     * Remove and an object by id
     *
     * @param id id
     */
    public void removeObjectById(int id) {
        final AbstractInteractableWorldObject object = interactableWorldObjects.get(id);
        if (object != null) {
            removeInteraction(object);
        } else {
            GameLogging.warn(this, "Failed to remove a network object by ID (%d)", id);
        }
    }

    /**
     * @return all destroyed objects
     */
    public Bag<DestroyedObject> destroyedWorldObjects() {
        return destroyedWorldObjects;
    }

    /**
     * Spawn a world object
     *
     * @param object   object
     * @param texture  texture
     * @param position position
     */
    public void spawnWorldObject(AbstractInteractableWorldObject object, String texture, Vector2 position) {
        object.setWorldIn(this);
        object.setPosition(position.x, position.y);

        if (texture != null) {
            object.setTextureAndSize(texture, game.asset().get(texture));
            // make sure this data is saved.
            if (object instanceof OpenableContainerInteraction container) {
                container.setActiveTexture(texture);
            }
        }

        object.load(game.asset());
        interactableWorldObjects.put(assignUniqueObjectId(object), object);

        if (object.isKeyable()) {
            keyListeners.add(object.keys());
        } else if (object.isMouseable()) {
            mouseListeners.add(object.mouse());
        }
    }

    /**
     * Load particle effects for a world object, interactable or not.
     *
     * @param wb     wb
     * @param object obj
     * @param asset  assets
     */
    protected void createObjectParticles(AbstractWorldObject wb, MapObject object, Asset asset) {
        final String particleKey = TiledMapLoader.ofString(object, "particle");
        if (particleKey == null) return; // this object has no particles

        final float xOffset = TiledMapLoader.ofFloat(object, "x_offset");
        final float yOffset = TiledMapLoader.ofFloat(object, "y_offset");

        final ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(particleKey), asset.getAtlasAssets());
        effect.setPosition(wb.getPosition().x + (wb.getSize().x / 2f) + xOffset, wb.getPosition().y + (wb.getSize().y / 2f) + yOffset);
        wb.addEffect(effect);
    }

    /**
     * Create a collision body for the provided object
     *
     * @param wb        wb
     * @param rectangle the rectangle shape
     */
    protected void createObjectCollisionBody(AbstractWorldObject wb, Rectangle rectangle) {
        final Body body = CollisionShapeCreator
                .createPolygonShapeInWorld(
                        rectangle.x,
                        rectangle.y,
                        rectangle.width,
                        rectangle.height,
                        OasisGameSettings.SCALE,
                        true,
                        world);
        wb.setBody(body);
    }

    /**
     * Create a collision body for the object using the textures bounds
     *
     * @param wb        object
     * @param rectangle rectangle shape - unused, for now,
     * @param texture   texture
     * @param x         x
     * @param y         y
     */
    protected void createObjectCollisionBodyFromTexture(AbstractWorldObject wb, Rectangle rectangle, TextureRegion texture, float x, float y) {
        if (texture == null) return;

        // create collision body, offset position to fit within bounds.
        final Body body = CollisionShapeCreator.createPolygonShapeInWorld(
                x,
                y,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                OasisGameSettings.SCALE,
                true,
                world);


        wb.setBody(body);
    }

    /**
     * Load interiors within this world
     *
     * @param worldMap   map
     * @param worldScale scale
     */
    protected void createInteriors(TiledMap worldMap, float worldScale) {
        final boolean result = TiledMapLoader.loadMapObjects(worldMap, worldScale, "Interior", (object, bounds) -> {
            final boolean enterable = object.getProperties().get("enterable", false, Boolean.class);
            final String asset = object.getProperties().get("interior_asset", null, String.class);
            final String typeString = object.getProperties().get("interior_type", null, String.class);
            final Interior type = Interior.of(typeString);

            if (asset != null) {
                final String cursorType = object.getProperties().get("cursor", "default", String.class).toUpperCase();
                final Cursor cursor = Cursor.valueOf(cursorType);

                // if this interior is locked/requires lock picking
                final boolean locked = TiledMapLoader.ofBoolean(object, "locked");
                final LockDifficulty difficulty = locked ? LockDifficulty.valueOf(TiledMapLoader.ofString(object, "lock_difficulty")) : null;

                final GameWorldInterior interior = type.createInterior(this, asset, cursor, bounds);
                interior.setEnterable(enterable);

                interior.setLocked(locked);
                interior.setLockDifficulty(difficulty);

                interiorWorlds.put(type, interior);
                GameLogging.info(this, "Loaded interior: %s", type);
            }
        });

        if (result) GameLogging.info(this, "Loaded %d instances.", interiorWorlds.size());
    }

    /**
     * Get an interior world by type
     *
     * @param type the type
     * @return the interior or {@code null} if none
     */
    public GameWorldInterior findInteriorByType(Interior type) {
        return interiorWorlds.get(type);
    }

    /**
     * @return map of all interior worlds
     */
    public EnumMap<Interior, GameWorldInterior> interiorWorlds() {
        return interiorWorlds;
    }

    /**
     * @return the interaction manager for this world.
     */
    public InteractionManager interactionManager() {
        return interactionManager;
    }

    /**
     * Register/create goals for an entity
     *
     * @param worldMap   map
     * @param worldScale scale
     */
    protected void createEntityGoals(TiledMap worldMap, float worldScale) {
        final boolean result = TiledMapLoader.loadMapObjects(worldMap, worldScale, "EntityGoals", (object, bounds) -> {
            final String e = TiledMapLoader.ofString(object, "entity");
            if (e != null) {
                // TODO EM-117: Will not work with multiple entities
                final EntityType type = EntityType.valueOf(e);

                final String g = TiledMapLoader.ofString(object, "goal");
                final String r = TiledMapLoader.ofString(object, "rotation");
                final EntityGoal goal = EntityGoal.valueOf(g);
                final EntityRotation goalRotation = r != null ? EntityRotation.valueOf(r) : null;

                final GameEntity entity = findEntity(type);
                entity.registerGoal(goal, new Vector2(bounds.x, bounds.y), goalRotation);
            }
        });

        if (result) GameLogging.info(this, "Loaded entity goals.");
    }

    /**
     * Find and create the entity paths for their AI
     *
     * @param worldMap   map
     * @param worldScale scale
     */
    protected void buildEntityPathing(TiledMap worldMap, float worldScale) {
        final MapLayer layer = worldMap.getLayers().get("Paths");
        if (layer == null) return;

        final Vector2[] paths = TiledMapLoader.loadPolyPath(layer, worldScale);

        if (paths == null) {
            GameLogging.warn(this, "Failed to load paths!");
        } else {
            this.paths.addAll(paths);
        }
    }

    /**
     * @return map of pathing for entities
     */
    public Array<Vector2> getPaths() {
        return paths;
    }

    public boolean isPaused() {
        return paused;
    }

    /**
     * Update and render this world.
     *
     * @param delta delta
     */
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        if (paused) {
            // render, no update
            renderWorld(delta);
        } else {
            //render+update normally
            tickWorld(delta);
            renderWorld(delta);
        }
    }

    /**
     * Update and render this world
     *
     * @param delta the delta
     */
    public void tickWorld(float delta) {
        final long now = System.nanoTime();
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(now - lastTick);

        boolean capture = false;
        if (elapsed >= 50) {
            lastTick = now;
            GameManager.tick++;
            capture = true;
        }

        performanceCounter.start();
        delta = tickWorldPhysicsSim(delta);

        // internal ticking, mostly interiors
        tick(delta);

        GdxAI.getTimepiece().update(delta);
        systemManager.update(delta);
        projectileManager.update(delta);
        worldDamageAnimator.update(delta);

        player.setPreviousPosition(player.getPosition());
        player.interpolatePosition();
        player.update(delta);

        interactionManager.updateNearestInteractions(this);

        if (capture && game.isHostingMultiplayerGame()) {
            // captures the game state of this world to be broadcast ASAP.
            final NetworkState state = game.hostNetwork().captureNetworkState(this);
            game.integratedServer().storeNetworkState(this, state);
            game.integratedServer().captureSnapshotSync(this);
        }

        // update host network tasks like player position
        if (game.isHostingMultiplayerGame()) game.hostNetwork().update();
        // interpolate player positions
        if (!game.isSingleplayerGame()) networkRenderer.update(this, delta);

        performanceCounter.stop();
        performanceCounter.tick(delta);
    }

    /**
     * Render this world
     *
     * @param delta delta
     */
    public void renderWorld(float delta) {
        renderer.beginRendering();
        renderer.render();

        if (!game.isSingleplayerGame()) {
            networkRenderer.render(this, batch, delta);
        }

        for (AbstractWorldObject object : this.worldObjects.values()) object.render(batch, delta);
        for (AbstractInteractableWorldObject worldObject : interactableWorldObjects.values())
            if (worldObject.render()) worldObject.render(batch, delta);

        // render entity UI elements
        for (GameEntity entity : entities.values()) {
            if (entity.isInView(renderer.getCamera())) {
                entity.render(batch, delta);
                entity.renderHealthBar(batch);
            }
        }

        // render particles
        for (ParticleEffect effect : effects) {
            effect.update(delta);
            effect.draw(batch);
        }

        projectileManager.render(batch, delta);

        // render local player next
        player.render(batch, delta);

        endRender();
    }

    /**
     * End world rendering and render GUI(s)
     */
    private void endRender() {
        batch.setProjectionMatrix(guiManager.getCamera().combined);

        // render object UI elements
        for (AbstractInteractableWorldObject worldObject : interactableWorldObjects.values()) {
            if (worldObject.isUiComponent() && worldObject.render()) {
                guiManager.renderWorldObjectComponents(worldObject, renderer.getCamera(), cursorInWorld, batch);
            }
        }

        // render entity UI elements
        for (GameEntity entity : entities.values())
            entity.postRender(renderer.getCamera(), guiManager.getCamera(), batch, worldDamageAnimator);

        // draw name tags on top of everything
        networkRenderer.postRender(this, batch, guiManager);

        batch.end();

        updateMouseListeners();
        guiManager.updateAndRender();

        if (OasisGameSettings.DRAW_DEBUG) boxRenderer.render(world, renderer.getCamera().combined);
    }

    /**
     * Update the mouse position within the world and fire all valid mouse events.
     */
    protected void updateMouseListeners() {
        // do not update cursor state if any GUI is visible besides the HUD
        if (guiManager.isAnyGuiVisible(GuiType.HUD)) return;

        renderer.getCamera().unproject(cursorInWorld.set(Gdx.input.getX(), Gdx.input.getY(), 0));

        // update all mouse listeners, only fire events if the event is not entered yet.
        for (MouseListener listener : mouseListeners) {
            if (listener.acceptsMouse() && !listener.hasEntered() && listener.within(cursorInWorld)) {
                guiManager.setCursorInGame(listener.enter(cursorInWorld));
            } else if (listener.acceptsMouse() && listener.hasEntered() && !listener.within(cursorInWorld)) {
                listener.exit(cursorInWorld);
                guiManager.resetCursor();
            }
        }
    }

    /**
     * Get an {@link EntityInteractable} by their NPC type
     *
     * @param type the type
     * @return the {@link EntityInteractable} or {@code  null} if not found
     */
    public EntityInteractable findInteractableEntity(EntityType type) {
        for (GameEntity entity : entities.values()) {
            if (entity.isInteractableEntity() && entity.asInteractable().type() == type) {
                return entity.asInteractable();
            }
        }
        return null;
    }

    /**
     * Find an enemy
     *
     * @param type type
     * @return the enemy or {@code null} if not found
     */
    public EntityEnemy findEnemy(EntityType type) {
        for (GameEntity entity : entities.values()) {
            if (entity instanceof EntityEnemy enemy && enemy.type() == type) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Find a regular entity
     *
     * @param type type
     * @return the entity or {@code null} if not found
     */
    public GameEntity findEntity(EntityType type) {
        for (GameEntity entity : entities.values()) {
            if (entity.type() == type) {
                return entity;
            }
        }
        return null;
    }

    public GameEntity findEntityById(int id) {
        return entities.get(id);
    }

    /**
     * Add a nearby entity
     *
     * @param entity the entity
     */
    public void addNearbyEntity(GameEntity entity) {
        nearbyEntities.put(entity.entityId(), entity);
    }

    /**
     * Remove a nearby entity
     *
     * @param entity the entity
     */
    public void removeNearbyEntity(GameEntity entity) {
        nearbyEntities.remove(entity.entityId());
    }

    /**
     * @return map of all nearby entities
     */
    public IntMap<GameEntity> nearbyEntities() {
        return nearbyEntities;
    }

    /**
     * Enable an interaction that is currently disabled.
     * Used for instances where an item or an entity needs to be used/spoken to first.
     *
     * @param type the type
     * @param key  the key
     */
    public void enableWorldInteraction(WorldInteractionType type, String key) {
        for (AbstractInteractableWorldObject interaction : interactableWorldObjects.values()) {
            if (interaction.matches(type, key)) {
                interaction.enable();
                break;
            }
        }
    }

    /**
     * Find an interaction
     *
     * @param type type
     * @param key  key
     * @return the object or {@code null} if not found
     */
    public AbstractInteractableWorldObject findInteraction(WorldInteractionType type, String key) {
        for (AbstractInteractableWorldObject interaction : interactableWorldObjects.values()) {
            if (interaction.matches(type, key)) {
                return interaction;
            }
        }
        return null;
    }

    /**
     * @return all (non) interactable world objects
     */
    public Collection<AbstractWorldObject> worldObjects() {
        return worldObjects.values();
    }

    public IntMap<AbstractInteractableWorldObject> interactableWorldObjects() {
        return interactableWorldObjects;
    }

    /**
     * Check if the provided item hit an entity when swung
     *
     * @param item the item
     * @return the entity if hit
     */
    public EntityEnemy hasHitEntity(ItemWeapon item) {
        for (GameEntity entity : entities.values()) {
            if (entity instanceof EntityEnemy enemy) {
                if (enemy.bb().overlaps(item.getBounds())) {
                    return enemy;
                }
            }
        }
        return null;
    }

    /**
     * Check if the player hit a breakable object
     *
     * @param item item
     * @return {@code null} if no object was hit
     */
    public BreakableObjectInteraction hitInteractableObject(ItemWeapon item) {
        for (AbstractInteractableWorldObject obj : interactableWorldObjects.values()) {
            if (obj instanceof BreakableObjectInteraction interaction) {
                if (interaction.playerHit(item)) {
                    return interaction;
                }
            }
        }
        return null;
    }

    /**
     * Resize this world
     *
     * @param width  width
     * @param height height
     */
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    /**
     * Pause this world.
     */
    public void pause() {
        GameLogging.info(this, "Pausing game.");
        paused = true;
    }

    /**
     * Resume this world.
     */
    public void resume() {
        GameLogging.info(this, "Resuming game.");
        paused = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.E) {
            if (interactionManager.handleInteractionKeyPress(this)) {
                return true;
            }
        }

        Keybindings.handleKeyInWorld(this, keycode);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (guiManager.isAnyGuiVisible(GuiType.HUD)) return true;

        for (MouseListener listener : mouseListeners) {
            if (listener.acceptsMouse() && listener.within(cursorInWorld)) {
                return listener.clicked(cursorInWorld);
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        entities.forEach(entity -> entity.value.dispose());
        worldObjects.values().forEach(Disposable::dispose);
        interactableWorldObjects.values().forEach(Disposable::dispose);

        mouseListeners.clear();
        keyListeners.clear();

        game.multiplexer().removeProcessor(this);

        unloadBox2dWorld();

        nearbyEntities.clear();
        entities.clear();
        worldObjects.clear();
        interactableWorldObjects.clear();
        effects.clear();
        paths.clear();

        systemManager.dispose();
        engine.removeAllSystems();
    }
}
