package io.github.masamune.save

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.maps.tiled.TiledMap
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.mutableEntityBagOf
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.CharacterStats.Companion.toItemStats
import io.github.masamune.component.Combat
import io.github.masamune.component.Equipment
import io.github.masamune.component.Experience
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.MonsterBook
import io.github.masamune.component.Move
import io.github.masamune.component.Name
import io.github.masamune.component.Physic
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.component.Remove
import io.github.masamune.component.Tiled
import io.github.masamune.component.Transform
import io.github.masamune.event.BeforeMapChangeEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.EventService
import io.github.masamune.event.LoadEvent
import io.github.masamune.event.MapChangeEvent
import io.github.masamune.event.MapTransitionAfterObjectLoadEvent
import io.github.masamune.event.MapTransitionBeginEvent
import io.github.masamune.event.MapTransitionEndEvent
import io.github.masamune.event.PlayerInteractCombatBeginEvent
import io.github.masamune.event.SaveEvent
import io.github.masamune.scheduledTask
import io.github.masamune.tiledmap.ActionType
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.tiledmap.TiledService.Companion.TILED_MAP_ASSET_PROPERTY_KEY
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.set
import ktx.tiled.property
import java.util.*

@Serializable
private data class PlayerState(
    val stats: ItemStats,
    val experience: Experience,
    val questLog: QuestLog,
    val equipment: List<ItemType>,
    val inventory: Map<ItemType, Int>,
    val talons: Int,
    val name: Name,
    val combatActions: List<ActionType>,
    val moveSpeed: Float,
    val position: Pair<Float, Float>,
    val monsterBook: MonsterBook?,
) {
    companion object {
        fun fromEntity(world: World, player: Entity) = with(world) {
            return@with PlayerState(
                player[CharacterStats].toItemStats(),
                player[Experience],
                player[QuestLog],
                player[Equipment].items.map { it[Item].type },
                player[Inventory].items.associate { it[Item].type to it[Item].amount },
                player[Inventory].talons,
                player[Name],
                player[Combat].availableActionTypes,
                player[Move].speed,
                player[Transform].position.x to player[Transform].position.y,
                player.getOrNull(MonsterBook),
            )
        }
    }
}

@Serializable
private data class MapState(
    val mapObjectIDs: List<Int>,
)

@Serializable
private data class SaveState(
    val playerState: PlayerState,
    val currentMap: TiledMapAsset,
    val mapData: Map<TiledMapAsset, MapState>,
)

class SaveService(
    private val tiledService: TiledService,
    private val eventService: EventService,
) : EventListener {

    private val savePreferences: Preferences by lazy { Gdx.app.getPreferences("masamune_save") }
    private val settingsPreferences: Preferences by lazy { Gdx.app.getPreferences("masamune_settings") }
    private var activeSaveState: SaveState? = null

    fun hasNoSaveState(): Boolean = KEY_SATE !in savePreferences

    fun hasSaveState(): Boolean = KEY_SATE in savePreferences

    fun clearSaveState() {
        savePreferences.flush {
            this.remove(KEY_SATE)
        }
    }

    fun saveAudioSettings(audioService: AudioService) {
        settingsPreferences.flush {
            this["music"] = audioService.musicVolume
            this["sound"] = audioService.soundVolume
        }
    }

    fun loadAudioSettings(audioService: AudioService) {
        audioService.musicVolume = settingsPreferences.getFloat("music", 0.25f)
        audioService.soundVolume = settingsPreferences.getFloat("sound", 0.5f)
    }

    fun saveLocale(locale: Locale) {
        settingsPreferences.flush {
            this["locale"] = locale.language
        }
    }

    fun loadLocale(): Locale {
        val locale = Locale(settingsPreferences.getString("locale", Locale.getDefault().language))
        if (locale.language == "de") {
            return locale
        }
        return Locale("", "", "")
    }

    override fun onEvent(event: Event) {
        when (event) {
            is SaveEvent -> save(event.world)
            is MapTransitionBeginEvent -> {
                save(event.world)
            }
            is MapTransitionEndEvent -> {
                save(event.world)
            }

            is PlayerInteractCombatBeginEvent -> {
                if (!event.autoSave) {
                    return
                }
                save(event.world)
            }
            is LoadEvent -> load(event.world)
            is BeforeMapChangeEvent -> removeTiledObjects(event.tiledMap, event.world)
            is MapTransitionAfterObjectLoadEvent -> removeTiledObjects(event.toTiledMap, event.world)
            else -> Unit
        }
    }

    private fun removeTiledObjects(tiledMap: TiledMap, world: World) {
        val saveState = activeSaveState ?: return

        val mapType = tiledMap.property<TiledMapAsset>(TILED_MAP_ASSET_PROPERTY_KEY)
        val mapState = saveState.mapData[mapType]
        if (mapState == null) {
            return
        } else if (mapState.mapObjectIDs.isEmpty()) {
            world.family { all(Tiled) }.forEach { it.remove() }
        } else {
            with(world) {
                family { all(Tiled) }
                    .filter { it[Tiled].id !in mapState.mapObjectIDs && it[Tiled].mapAsset == mapType }
                    .forEach { it.remove() }
            }
        }
    }

    private fun load(world: World) {
        if (KEY_SATE !in savePreferences) {
            log.error { "Trying to load a non-existing save state." }
            activeSaveState = null
            return
        }

        val saveState = Json.decodeFromString<SaveState>(savePreferences.getString(KEY_SATE))
        activeSaveState = saveState
        log.info { "Loading save state: $saveState" }

        // remove all existing entities
        world.removeAll(true)

        // load map to also get a player entity
        tiledService.unloadActiveMap(world)
        tiledService.loadMap(saveState.currentMap).also {
            tiledService.setMap(it, world, withMapChangeEvent = false)
        }

        // configure player
        val player = world.family { all(Player) }.single()
        with(world) {
            player.configure { entity ->
                entity += saveState.playerState.experience
                entity += saveState.playerState.questLog.also { questLog ->
                    // register quests with a delay to avoid concurrent modification of eventService
                    scheduledTask(0.01f) {
                        questLog.quests
                            .filter { !it.isCompleted() }
                            .forEach { eventService += it }
                    }
                }
                entity += saveState.playerState.name
                entity += Equipment(
                    mutableEntityBagOf(
                        *saveState.playerState.equipment
                            .map { tiledService.loadItem(world, it) }
                            .toTypedArray()
                    )
                )
                entity += Inventory(
                    talons = saveState.playerState.talons,
                    items = mutableEntityBagOf(
                        *saveState.playerState.inventory
                            .map { (type, amount) -> tiledService.loadItem(world, type, amount) }
                            .toTypedArray()
                    )
                )
                entity += CharacterStats.fromItemStats(saveState.playerState.stats)
                saveState.playerState.monsterBook?.let { monsterBook ->
                    entity += monsterBook
                }
            }
            player[Combat].availableActionTypes = saveState.playerState.combatActions
            player[Move].speed = saveState.playerState.moveSpeed
            val (x, y) = saveState.playerState.position
            player[Physic].run {
                body.setTransform(x, y, body.angle)
                prevPosition.set(x, y)
            }
            player[Transform].position.run {
                this.x = x
                this.y = y
            }
        }

        // fire map change event now AFTER player is configured (for correct map trigger behavior)
        removeTiledObjects(tiledService.activeMap, world)
        eventService.fire(MapChangeEvent(tiledService.activeMap, ignoreTrigger = false, world))
    }

    private fun save(world: World) {
        val mapData: MutableMap<TiledMapAsset, MapState> = if (KEY_SATE in savePreferences) {
            val state: SaveState = Json.decodeFromString(savePreferences.getString(KEY_SATE))
            state.mapData.toMutableMap()
        } else {
            mutableMapOf()
        }

        val player = world.family { all(Player) }.single()
        val playerState = PlayerState.fromEntity(world, player)
        val currentMap: TiledMapAsset = tiledService.activeMap.property<TiledMapAsset>(TILED_MAP_ASSET_PROPERTY_KEY)
        val mapObjectIDs: List<Int> = with(world) {
            family { all(Tiled).none(Remove) }.map { it[Tiled].id }
        }
        mapData[currentMap] = MapState(mapObjectIDs)
        val saveState = SaveState(playerState, currentMap, mapData)
        activeSaveState = saveState
        val saveStateStr = Json.encodeToString(saveState)
        log.debug { "Saving state: $saveStateStr" }
        savePreferences.flush {
            this[KEY_SATE] = saveStateStr
        }
    }

    companion object {
        private val log = logger<SaveService>()
        private const val KEY_SATE = "state"
    }
}
