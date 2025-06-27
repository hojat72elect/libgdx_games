package io.github.masamune.tiledmap

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FileTextureData
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.mutableEntityBagOf
import io.github.masamune.Masamune.Companion.UNIT_SCALE
import io.github.masamune.ai.AnimationStateIdle
import io.github.masamune.ai.FleksStateMachine
import io.github.masamune.ai.cyclopsBehavior
import io.github.masamune.ai.defaultBehavior
import io.github.masamune.ai.spiderBehavior
import io.github.masamune.ai.supportBehavior
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.component.AI
import io.github.masamune.component.Animation
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Combat
import io.github.masamune.component.Enemy
import io.github.masamune.component.Equipment
import io.github.masamune.component.Experience
import io.github.masamune.component.Facing
import io.github.masamune.component.FacingDirection
import io.github.masamune.component.FollowPath
import io.github.masamune.component.Graphic
import io.github.masamune.component.Interact
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.Move
import io.github.masamune.component.Name
import io.github.masamune.component.Physic
import io.github.masamune.component.Player
import io.github.masamune.component.Portal
import io.github.masamune.component.QuestLog
import io.github.masamune.component.State
import io.github.masamune.component.Tag
import io.github.masamune.component.Tiled
import io.github.masamune.component.Transform
import io.github.masamune.component.Trigger
import io.github.masamune.event.BeforeMapChangeEvent
import io.github.masamune.event.EventService
import io.github.masamune.event.MapChangeEvent
import ktx.app.gdxError
import ktx.log.logger
import ktx.math.vec2
import ktx.math.vec3
import ktx.tiled.height
import ktx.tiled.id
import ktx.tiled.isEmpty
import ktx.tiled.layer
import ktx.tiled.property
import ktx.tiled.propertyOrNull
import ktx.tiled.set
import ktx.tiled.width
import ktx.tiled.x
import ktx.tiled.y
import kotlin.system.measureTimeMillis

enum class ObjectLayer {
    OBJECT, PORTAL, TRIGGER, PATH, CUT_SCENE_OBJECTS;

    val tiledName: String = this.name.lowercase()
}

class TiledService(
    val assetService: AssetService,
    val eventService: EventService,
) {
    private var currentMap: TiledMap? = null
    private val staticCollisionBodies = mutableListOf<Body>()

    val activeMap: TiledMap
        get() = currentMap ?: gdxError("Trying to access active map before loading any map")

    fun loadMap(asset: TiledMapAsset): TiledMap {
        val loadingTime = measureTimeMillis {
            assetService.load(asset)
            assetService.finishLoading()
        }
        log.debug { "Loading of map $asset took $loadingTime ms" }

        // set the TiledMapAsset as property on the TiledMap to make it easier to identify it
        return assetService[asset].also {
            it.properties[TILED_MAP_ASSET_PROPERTY_KEY] = asset
        }
    }

    fun setMap(
        tiledMap: TiledMap,
        world: World,
        withBoundaries: Boolean = true,
        withTriggers: Boolean = true,
        withPortals: Boolean = true,
        withMapChangeEvent: Boolean = true,
    ) {
        currentMap?.let { unloadMap(it, world) }
        currentMap = tiledMap
        val tiledMapAsset: TiledMapAsset = tiledMap.property(TILED_MAP_ASSET_PROPERTY_KEY)

        if (withBoundaries) {
            staticCollisionBodies += tiledMap.spawnBoundaryBody(world)
        }
        loadGroundCollision(tiledMap, world)
        loadObjects(tiledMap, world)
        if (withTriggers) {
            tiledMap[ObjectLayer.TRIGGER]?.objects?.forEach { loadTrigger(it, world, tiledMapAsset) }
        }
        if (withPortals) {
            tiledMap[ObjectLayer.PORTAL]?.objects?.forEach { loadPortal(it, world, tiledMapAsset) }
        }
        if (withMapChangeEvent) {
            eventService.fire(BeforeMapChangeEvent(tiledMap, world))
            eventService.fire(MapChangeEvent(tiledMap, ignoreTrigger = !withTriggers, world))
        }
    }

    fun loadObjects(tiledMap: TiledMap, world: World): List<Entity> {
        if (tiledMap.objectsLoaded) {
            // objects already loaded
            return emptyList()
        }

        val mapObjects = tiledMap[ObjectLayer.OBJECT]?.objects ?: return emptyList()

        // at this point we know that mapObjects are not null
        val result = mutableListOf<Entity>()
        val tiledMapAsset = tiledMap.property<TiledMapAsset>(TILED_MAP_ASSET_PROPERTY_KEY)
        mapObjects.forEach {
            result += loadObject(it, world, tiledMapAsset)
        }
        tiledMap.objectsLoaded = true
        return result
    }

    fun unloadBoundaryAndGroundCollision() {
        staticCollisionBodies.forEach { it.world.destroyBody(it) }
        staticCollisionBodies.clear()
    }

    fun unloadNonPlayerBodies(world: World) {
        val nonPlayerEntities = world.family { none(Player).all(Physic) }
        log.debug { "Unloading ${nonPlayerEntities.numEntities} non-player bodies" }
        nonPlayerEntities.forEach { entity ->
            entity.configure { it -= Physic }
        }
    }

    fun unloadActiveMap(world: World) {
        val map = currentMap ?: return
        unloadMap(map, world)
        currentMap = null
    }

    private fun unloadMap(tiledMap: TiledMap, world: World) {
        val tiledMapAsset = tiledMap.property<TiledMapAsset>(TILED_MAP_ASSET_PROPERTY_KEY)
        val unloadingTime = measureTimeMillis {
            assetService.unload(tiledMapAsset)
            assetService.finishLoading()
        }
        log.debug { "Unloading of map $tiledMapAsset took $unloadingTime ms" }

        unloadObjects(world)
    }

    private fun unloadObjects(world: World) {
        // unloading static collision bodies
        unloadBoundaryAndGroundCollision()

        // unloading non-player entities (player entities are taken over to new map)
        val nonPlayerEntities = world.family { none(Player, Tag.MAP_TRANSITION).all(Tiled) }
        log.debug { "Unloading ${nonPlayerEntities.numEntities} entities" }
        nonPlayerEntities.forEach { entity ->
            log.debug { "Unloading entity $entity" }
            entity.remove()
        }
    }

    private inline fun TiledMap.forEachCell(action: (cell: Cell, cellX: Int, cellY: Int) -> Unit) {
        layers.filterIsInstance<TiledMapTileLayer>()
            .forEach { layer ->
                for (x in 0 until layer.width) {
                    for (y in 0 until layer.height) {
                        layer.getCell(x, y)?.let { action(it, x, y) }
                    }
                }
            }
    }

    private fun loadGroundCollision(tiledMap: TiledMap, world: World) {
        tiledMap.forEachCell { cell, cellX, cellY ->
            if (cell.tile.objects.isEmpty()) {
                // no collision objects -> nothing to do
                return@forEachCell
            }

            val body = cell.tile.toBody(world, cellX.toFloat(), cellY.toFloat(), BodyType.StaticBody)
            staticCollisionBodies += body
        }
    }

    private fun loadPortal(
        mapObject: MapObject,
        world: World,
        tiledMapAsset: TiledMapAsset,
    ) {
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE
        val w = mapObject.width * UNIT_SCALE
        val h = mapObject.height * UNIT_SCALE
        val toMapName = mapObject.name ?: ""
        if (toMapName.isBlank()) {
            gdxError("Missing name for portal: ${mapObject.id}")
        }
        val toMapAsset = TiledMapAsset.entries.firstOrNull { it.name == toMapName }
            ?: gdxError("There is no TiledMapAsset of name $toMapName")

        world.entity { entity ->
            log.debug { "Loading portal ${mapObject.id} as entity $entity" }

            entity += Tiled(mapObject.id, TiledObjectType.PORTAL, tiledMapAsset)
            entity += Portal(toMapAsset, mapObject.targetPortalId)
            val body = mapObject.toBody(world, x, y, data = entity)
            entity += Physic(body)
            entity += Transform(vec3(body.position, 0f), vec2(w, h))
        }
    }

    private fun loadTrigger(
        mapObject: MapObject,
        world: World,
        tiledMapAsset: TiledMapAsset,
    ) {
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE
        val triggerName = mapObject.name ?: ""
        if (triggerName.isBlank()) {
            gdxError("Missing name for trigger: ${mapObject.id}")
        }

        world.entity { entity ->
            log.debug { "Loading trigger $triggerName as entity $entity" }

            entity += Trigger(triggerName)
            val body = mapObject.toBody(world, x, y, data = entity)
            entity += Physic(body)
            entity += Tiled(mapObject.id, TiledObjectType.TRIGGER, tiledMapAsset)
        }
    }

    private fun loadObject(mapObject: MapObject, world: World, tiledMapAsset: TiledMapAsset): Entity {
        val tiledObj = mapObject as TiledMapTileMapObject
        val isPlayerObj = mapObject.name == "Player"
        val playerFamily = world.family { all(Player) }
        if (isPlayerObj && playerFamily.isNotEmpty) {
            log.debug { "Player already loaded -> ignore object" }
            return playerFamily.first()
        }

        val tile = tiledObj.tile ?: gdxError("Object ${mapObject.id} is not linked to a tile")
        val x = tiledObj.x * UNIT_SCALE
        val y = tiledObj.y * UNIT_SCALE

        val objTypeStr = tile.objType
        val objType = TiledObjectType.valueOf(objTypeStr)
        val tiledClass: String = tile.property("type", "")
        return when (tiledClass) {
            "EnemyObject" -> loadGameEnemy(x, y, objType, tile, tiledObj, world, tiledMapAsset)
            "NpcObject" -> loadNpc(x, y, objType, tile, tiledObj, world, tiledMapAsset)
            "PropObject" -> loadPropObject(x, y, objType, tile, tiledObj, world, tiledMapAsset)
            "PlayerObject" -> loadPlayerObject(x, y, tile, tiledObj, world)
            else -> gdxError("Unsupported tile class $tiledClass for tile ${tile.id}")
        }
    }

    private fun loadPlayerObject(
        x: Float,
        y: Float,
        tile: TiledMapTile,
        tiledObj: TiledMapTileMapObject,
        world: World
    ): Entity = world.entity {
        log.debug { "Loading player ${tiledObj.id} as entity $it" }

        it += Facing(FacingDirection.DOWN)
        val graphicCmp = configureGraphic(it, tile, AnimationType.IDLE.name)
        it += Transform(position = vec3(x, y, 0f), size = graphicCmp.regionSize)
        configureMove(it, tile)
        configurePhysic(it, tile, world, x, y, BodyType.DynamicBody.name)
        configurePlayer(world, it)
        configureCharacterStats(it, tile)
        configureCombat(it, tile)
    }

    private fun loadPropObject(
        x: Float,
        y: Float,
        objType: TiledObjectType,
        tile: TiledMapTile,
        tiledObj: TiledMapTileMapObject,
        world: World,
        tiledMapAsset: TiledMapAsset,
    ): Entity = world.entity {
        log.debug { "Loading prop ${tiledObj.id} as entity $it" }

        configureTiled(it, tiledObj.id, objType, tiledMapAsset)
        it += Facing(FacingDirection.DOWN)

        val animation = tiledObj.property("animation", "")
        val aniType = when {
            animation.isNotEmpty() -> AnimationType.valueOf(animation.uppercase())
            else -> AnimationType.UNDEFINED
        }
        configureGraphic(it, tile, aniType.name, tiledObj.property("animationSpeed", 1f))
        it += Transform(
            position = vec3(x, y, 0f),
            size = vec2(tiledObj.width * UNIT_SCALE, tiledObj.height * UNIT_SCALE),
        )
        configurePhysic(it, tile, world, x, y, BodyType.StaticBody.name)
        configureTrigger(it, tiledObj)
    }

    fun loadNpc(location: Vector2, type: TiledObjectType, world: World): Entity {
        val objectsTileSet = currentMap.objectsTileSet()
        objectsTileSet.iterator().forEach { tile ->
            if (tile.objType != type.name) {
                return@forEach
            }

            val dummyObj = TiledMapTileMapObject(tile, false, false)
            dummyObj.properties["id"] = tile.id
            val tiledMapAsset = activeMap.property<TiledMapAsset>(TILED_MAP_ASSET_PROPERTY_KEY)
            return loadNpc(location.x, location.y, type, tile, dummyObj, world, tiledMapAsset)
        }

        gdxError("There is no enemy with type $type")
    }

    private fun loadNpc(
        x: Float,
        y: Float,
        objType: TiledObjectType,
        tile: TiledMapTile,
        tiledObj: TiledMapTileMapObject,
        world: World,
        tiledMapAsset: TiledMapAsset,
    ): Entity = world.entity {
        log.debug { "Loading NPC ${tiledObj.id} as entity $it" }

        configureTiled(it, tiledObj.id, objType, tiledMapAsset)
        it += Facing(FacingDirection.DOWN)
        val graphicCmp = configureGraphic(it, tile, AnimationType.WALK.name)
        it += Transform(position = vec3(x, y, 0f), size = graphicCmp.regionSize)
        configurePhysic(it, tile, world, x, y, BodyType.DynamicBody.name)
        configureTrigger(it, tile)
        configurePathAndMove(it, tiledObj)
    }

    private fun loadGameEnemy(
        x: Float,
        y: Float,
        objType: TiledObjectType,
        tile: TiledMapTile,
        tiledObj: TiledMapTileMapObject,
        world: World,
        tiledMapAsset: TiledMapAsset,
    ): Entity = world.entity {
        log.debug { "Loading game enemy ${tiledObj.id} as entity $it" }

        configureTiled(it, tiledObj.id, objType, tiledMapAsset)
        it += Facing(FacingDirection.DOWN)
        val graphicCmp = configureGraphic(it, tile, AnimationType.WALK.name)
        it += Transform(position = vec3(x, y, 0f), size = graphicCmp.regionSize)
        configurePhysic(it, tile, world, x, y, BodyType.KinematicBody.name)
        configureEnemy(it, tiledObj)
    }

    private fun EntityCreateContext.configureEnemy(entity: Entity, tiledObj: TiledMapTileMapObject) {
        val enemiesStr = tiledObj.property("enemies", "")
        if (enemiesStr.isBlank()) {
            gdxError("No enemies configured for object ${tiledObj.id}")
        }

        val combatEntities = enemiesStr.split(";").associate { str ->
            val splits = str.split("=")
            if (splits.size != 2) {
                gdxError("Wrong enemy format for object ${tiledObj.id}. Format must be 'TiledObjectType=number': $str")
            }
            val type = TiledObjectType.valueOf(splits[0])
            val amount = splits[1].toInt()
            type to amount
        }
        log.debug { "Encounter for object ${tiledObj.id}: $combatEntities" }
        entity += Enemy(combatEntities)
    }

    private fun EntityCreateContext.configureTiled(
        entity: Entity,
        tiledObjId: Int,
        objectType: TiledObjectType,
        tiledMapAsset: TiledMapAsset,
    ) {
        entity += Tiled(tiledObjId, objectType, tiledMapAsset)
    }

    private fun EntityCreateContext.configureGraphic(
        entity: Entity,
        tile: TiledMapTile,
        animationTypeStr: String,
        animationSpeed: Float = 1f,
    ): Graphic {
        val atlasStr = tile.atlas
        val atlasAsset = AtlasAsset.entries.firstOrNull { it.name == atlasStr }
            ?: gdxError("There is no atlas of name $atlasStr")
        val atlasRegionKey = tile.imageName
        if (atlasRegionKey.isBlank()) {
            gdxError("Missing atlasRegionKey for tile ${tile.id}")
        }
        val atlas = assetService[atlasAsset]
        val aniType = AnimationType.entries.firstOrNull { it.name == animationTypeStr }
            ?: gdxError("There is no animation type of name $animationTypeStr")

        // optional animation component
        val graphicCmpRegion: TextureRegion = if (aniType != AnimationType.UNDEFINED) {
            // add animation component
            val animationCmp = Animation.ofAtlas(
                atlas,
                atlasRegionKey,
                aniType,
                FacingDirection.DOWN,
                speed = animationSpeed
            )
            entity += animationCmp
            // use first frame for graphic component
            animationCmp.gdxAnimation.getKeyFrame(0f)
        } else {
            // no animation; use region for graphic component
            val texRegions = atlas.findRegions(atlasRegionKey)
            if (texRegions.isEmpty) {
                gdxError("No regions in atlas $atlasStr for key $atlasRegionKey")
            }
            texRegions.first()
        }

        // graphic component
        val color = Color(1f, 1f, 1f, 1f)
        val graphicCmp = Graphic(graphicCmpRegion, color)
        entity += graphicCmp
        return graphicCmp
    }

    private fun EntityCreateContext.configureMove(entity: Entity, tile: TiledMapTile) {
        val speed = tile.speed
        if (speed > 0f) {
            entity += Move(speed = speed)
        }
    }

    private fun EntityCreateContext.configurePhysic(
        entity: Entity,
        tile: TiledMapTile,
        world: World,
        objX: Float,
        objY: Float,
        bodyTypeStr: String,
    ) {
        if (tile.objects.isEmpty()) {
            // no collision objects -> nothing to do
            return
        }
        if (bodyTypeStr == "UNDEFINED") {
            gdxError("Physic object without defined 'bodyType' for tile ${tile.id}")
        }
        val bodyType = BodyType.valueOf(bodyTypeStr)

        val body = tile.toBody(world, objX, objY, bodyType, data = entity)
        entity += Physic(body)
    }

    private fun EntityCreateContext.configureTrigger(entity: Entity, tile: TiledMapTile) {
        if (tile.triggerName.isBlank()) {
            return
        }

        entity += Trigger(tile.triggerName)
    }

    private fun EntityCreateContext.configureTrigger(entity: Entity, tiledObj: TiledMapTileMapObject) {
        val trigger = tiledObj.propertyOrNull<String>("triggerName") ?: return
        entity += Trigger(trigger)
    }

    private fun MapObject.toPathVertices(): List<Vector2> {
        return when (this) {
            is PolygonMapObject -> this.polygon
                .transformedVertices
                .asList()
                .windowed(size = 2, step = 2) {
                    vec2(it[0] * UNIT_SCALE, it[1] * UNIT_SCALE)
                }

            is PolylineMapObject -> this.polyline
                .transformedVertices
                .asList()
                .windowed(size = 2, step = 2) {
                    vec2(it[0] * UNIT_SCALE, it[1] * UNIT_SCALE)
                }

            else -> gdxError("Path object must be a polygon or polyline map object")
        }
    }

    fun loadPath(pathId: Int): List<Vector2> {
        currentMap
            ?.let { tiledMap ->
                return tiledMap[ObjectLayer.PATH]
                    ?.objects
                    ?.single { it.id == pathId }
                    ?.toPathVertices()
                    ?: gdxError("There is no path of id $pathId in layer ${ObjectLayer.PATH}")
            }
            ?: gdxError("There is no active map loaded")
    }

    private fun EntityCreateContext.configurePathAndMove(entity: Entity, tiledObj: TiledMapTileMapObject) {
        val pathObj = tiledObj.propertyOrNull<MapObject>("path") ?: return
        entity += FollowPath(pathObj.toPathVertices())
        entity += Move(speed = tiledObj.property("pathSpeed", 3f))
    }

    private fun MapProperties?.toItemStats(): ItemStats? {
        val props = this ?: return null

        val agility = props.get("agility", 0f, Float::class.java)
        val arcaneStrike = props.get("arcaneStrike", 0f, Float::class.java)
        val armor = props.get("armor", 0f, Float::class.java)
        val constitution = props.get("constitution", 0f, Float::class.java)
        val criticalStrike = props.get("criticalStrike", 0f, Float::class.java)
        val damage = props.get("damage", 0f, Float::class.java)
        val intelligence = props.get("intelligence", 0f, Float::class.java)
        val life = props.get("life", 0f, Float::class.java)
        val lifeMax = props.get("lifeMax", 0f, Float::class.java)
        val magicalEvade = props.get("magicalEvade", 0f, Float::class.java)
        val mana = props.get("mana", 0f, Float::class.java)
        val manaMax = props.get("manaMax", 0f, Float::class.java)
        val physicalEvade = props.get("physicalEvade", 0f, Float::class.java)
        val resistance = props.get("resistance", 0f, Float::class.java)
        val strength = props.get("strength", 0f, Float::class.java)
        if (agility == 0f &&
            arcaneStrike == 0f &&
            armor == 0f &&
            constitution == 0f &&
            criticalStrike == 0f &&
            damage == 0f &&
            intelligence == 0f &&
            life == 0f &&
            lifeMax == 0f &&
            magicalEvade == 0f &&
            mana == 0f &&
            manaMax == 0f &&
            physicalEvade == 0f &&
            resistance == 0f &&
            strength == 0f
        ) {
            // all stats are null -> no need to add them to the entity
            return null
        }

        return ItemStats(
            agility = agility,
            arcaneStrike = arcaneStrike,
            armor = armor,
            constitution = constitution,
            criticalStrike = criticalStrike,
            damage = damage,
            intelligence = intelligence,
            life = life,
            lifeMax = lifeMax,
            magicalEvade = magicalEvade,
            mana = mana,
            manaMax = manaMax,
            physicalEvade = physicalEvade,
            resistance = resistance,
            strength = strength,
        )
    }

    private fun MapProperties?.toCharacterStats(): CharacterStats? {
        val props = this ?: return null

        val agility = props.get("agility", 0f, Float::class.java)
        val arcaneStrike = props.get("arcaneStrike", 0f, Float::class.java)
        val armor = props.get("armor", 0f, Float::class.java)
        val constitution = props.get("constitution", 0f, Float::class.java)
        val criticalStrike = props.get("criticalStrike", 0f, Float::class.java)
        val damage = props.get("damage", 0f, Float::class.java)
        val intelligence = props.get("intelligence", 0f, Float::class.java)
        val baseLife = props.get("baseLife", 0f, Float::class.java)
        val magicalEvade = props.get("magicalEvade", 0f, Float::class.java)
        val baseMana = props.get("baseMana", 0f, Float::class.java)
        val physicalEvade = props.get("physicalEvade", 0f, Float::class.java)
        val resistance = props.get("resistance", 0f, Float::class.java)
        val strength = props.get("strength", 0f, Float::class.java)
        if (agility == 0f &&
            arcaneStrike == 0f &&
            armor == 0f &&
            constitution == 0f &&
            criticalStrike == 0f &&
            damage == 0f &&
            intelligence == 0f &&
            baseLife == 0f &&
            magicalEvade == 0f &&
            baseMana == 0f &&
            physicalEvade == 0f &&
            resistance == 0f &&
            strength == 0f
        ) {
            // all stats are null -> no need to add them to the entity
            return null
        }

        return CharacterStats(
            agility = agility,
            arcaneStrike = arcaneStrike,
            armor = armor,
            constitution = constitution,
            criticalStrike = criticalStrike,
            baseDamage = damage,
            intelligence = intelligence,
            baseLife = baseLife,
            magicalEvade = magicalEvade,
            baseMana = baseMana,
            physicalEvade = physicalEvade,
            resistance = resistance,
            strength = strength,
        )
    }

    private fun EntityCreateContext.configureItemStats(entity: Entity, tile: TiledMapTile) {
        val itemStats = tile.stats.toItemStats() ?: ItemStats()
        entity += itemStats
    }

    private fun EntityCreateContext.configureCharacterStats(entity: Entity, tile: TiledMapTile) {
        val charStats = tile.stats.toCharacterStats() ?: return

        entity += charStats
    }

    private fun EntityCreateContext.configureAI(world: World, entity: Entity, tile: TiledMapTile) {
        val aiCmp = when (tile.behavior) {
            "default" -> AI(defaultBehavior(world, entity))
            "support" -> AI(supportBehavior(world, entity))
            "cyclops" -> AI(cyclopsBehavior(world, entity))
            "spider" -> AI(spiderBehavior(world, entity))
            else -> gdxError("Unsupported behavior ${tile.behavior}")
        }
        entity += aiCmp
    }

    private fun EntityCreateContext.configureEnemyInventory(entity: Entity, tile: TiledMapTile) {
        entity += Inventory(talons = tile.talons)
    }

    private fun EntityCreateContext.configureCombat(entity: Entity, tile: TiledMapTile) {
        val actionTypes = tile.combatActions
        if (actionTypes.isEmpty()) {
            return
        }

        entity += Combat(availableActionTypes = actionTypes.split(",").map { ActionType.valueOf(it) })
    }

    private fun EntityCreateContext.configurePlayer(world: World, entity: Entity) {
        log.debug { "Configuring player" }
        entity += Tag.CAMERA_FOCUS
        entity += Player()
        entity += Name("Alexxius")
        entity += Interact()
        entity += Inventory(
            talons = 100,
            items = mutableEntityBagOf(
                *PLAYER_START_ITEMS.map { loadItem(world, it.key, it.value) }.toTypedArray()
            )
        )
        entity += Equipment()
        entity += QuestLog()
        entity += State(FleksStateMachine(world, entity, AnimationStateIdle))
        entity += Experience()
    }

    private fun EntityCreateContext.configureItem(entity: Entity, tile: TiledMapTile, amount: Int = 1) {
        val itemType = ItemType.valueOf(tile.itemType)
        val itemAction = tile.action
        val actionType = if (itemAction.isEmpty()) {
            ActionType.UNDEFINED
        } else {
            ActionType.valueOf(itemAction)
        }

        entity += Item(
            type = itemType,
            cost = tile.cost,
            category = ItemCategory.valueOf(tile.category),
            actionType = actionType,
            descriptionKey = "item.${itemType.name.lowercase()}.description",
            consumableType = ConsumableType.valueOf(tile.consumableType),
            amount = amount,
        )
    }

    private fun TiledMap?.objectsTileSet(): TiledMapTileSet {
        return this?.tileSets?.singleOrNull { it.name.contains("objects", true) }
            ?: gdxError("Objects TileSet is not available for map $this")
    }

    fun loadItem(world: World, itemType: ItemType, amount: Int = 1): Entity {
        val objectsTileSet = currentMap.objectsTileSet()
        objectsTileSet.iterator().forEach { tile ->
            if (tile.itemType != itemType.name) {
                return@forEach
            }

            return world.entity {
                log.debug { "Loading item $itemType as entity $it" }

                it += Name(tile.itemType.lowercase())
                configureItemStats(it, tile)
                configureGraphic(it, tile, AnimationType.UNDEFINED.name)
                configureItem(it, tile, amount)
                configureMove(it, tile)
            }
        }

        gdxError("There is no item with type $itemType")
    }

    fun loadCombatEnemy(world: World, type: TiledObjectType, x: Float, y: Float): Entity {
        val objectsTileSet = currentMap.objectsTileSet()
        objectsTileSet.iterator().forEach { tile ->
            if (tile.objType != type.name) {
                return@forEach
            }

            val tiledMapAsset: TiledMapAsset = activeMap.property(TILED_MAP_ASSET_PROPERTY_KEY)
            return world.entity {
                log.debug { "Loading enemy $type as entity $it" }

                it += Tiled(tile.id, type, tiledMapAsset)
                val graphic = configureGraphic(it, tile, AnimationType.WALK.name)
                it += graphic
                it += Transform(vec3(x, y, 0f), graphic.regionSize)
                it += Name(tile.objType.lowercase())
                configureCharacterStats(it, tile)
                it += Facing(FacingDirection.DOWN)
                configureCombat(it, tile)
                configureEnemyInventory(it, tile)
                it += Experience(tile.level, tile.xp)
                configureAI(world, it, tile)
            }
        }

        gdxError("There is no enemy with type $type")
    }

    fun loadEnemyStats(type: TiledObjectType): Triple<CharacterStats, Int, Int> {
        val objectsTileSet = currentMap.objectsTileSet()
        objectsTileSet.iterator().forEach { tile ->
            if (tile.objType != type.name) {
                return@forEach
            }

            val charStats = tile.stats.toCharacterStats() ?: gdxError("There are no stats defined for $type")
            return Triple(charStats, tile.xp, tile.talons)
        }

        log.error { "There are no stats defined for $type" }
        return Triple(CharacterStats(), 0, 0)
    }

    fun loadPoint(pointName: String): Vector2 {
        currentMap
            ?.let { tiledMap ->
                val point = tiledMap[ObjectLayer.CUT_SCENE_OBJECTS]
                    ?.objects
                    ?.single { it.name == pointName }
                    ?: gdxError("There is no object with name $pointName in ${ObjectLayer.CUT_SCENE_OBJECTS} layer")
                return vec2(point.x * UNIT_SCALE, point.y * UNIT_SCALE)
            }
            ?: gdxError("There is no active map loaded")
    }

    companion object {
        private val log = logger<TiledService>()
        const val TILED_MAP_ASSET_PROPERTY_KEY = "tiledMapAsset"
        var PLAYER_START_ITEMS = mapOf(
            ItemType.SMALL_HEALTH_POTION to 3,
            ItemType.SMALL_MANA_POTION to 1,
        )

        fun TiledMap.portal(portalId: Int): MapObject {
            return layer("portal").objects
                .firstOrNull { it.id == portalId }
                ?: gdxError("There is no portal of id $portalId")
        }

        var TiledMap.objectsLoaded: Boolean
            get() = this.property("objectsLoaded", false)
            set(value) {
                this.properties["objectsLoaded"] = value
            }

        val TiledMapTile.imageName: String
            get() {
                val name = (textureRegion.texture.textureData as FileTextureData).fileHandle.nameWithoutExtension()
                return when {
                    property("type", "") == "ItemObject" -> "items/$name"
                    property("regionPrefix", "").isNotEmpty() -> "${property("regionPrefix", "")}/$name"
                    else -> name
                }
            }

        operator fun TiledMap.get(layer: ObjectLayer): MapLayer? = this.layers[layer.tiledName]
    }
}
