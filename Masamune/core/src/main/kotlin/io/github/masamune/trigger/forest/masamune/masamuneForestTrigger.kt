package io.github.masamune.trigger.forest.masamune

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.Masamune
import io.github.masamune.asset.MusicAsset
import io.github.masamune.asset.SoundAsset
import io.github.masamune.component.FacingDirection
import io.github.masamune.component.QuestLog
import io.github.masamune.component.Tiled
import io.github.masamune.component.Trigger
import io.github.masamune.event.EventService
import io.github.masamune.event.PlayerInteractEndContactEvent
import io.github.masamune.quest.MainQuest
import io.github.masamune.screen.FadeTransitionType
import io.github.masamune.screen.MainMenuScreen
import io.github.masamune.tiledmap.TiledObjectType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger
import ktx.math.minus
import ktx.math.vec2

fun World.masamuneForestTrigger(
    name: String,
    scriptEntity: Entity,
    triggeringEntity: Entity
): TriggerScript = trigger(name, this, triggeringEntity) {
    val tiledFamily = family { all(Tiled) }
    val tiledService = inject<TiledService>()

    // remove trigger from masamune to remove outline and make it not interactable anymore after the cutscene
    scriptEntity.configure { it -= Trigger }
    world.inject<EventService>().fire(PlayerInteractEndContactEvent(triggeringEntity, scriptEntity))
    // update main quest
    triggeringEntity[QuestLog].get<MainQuest>().progress = 50

    actionFadeOutMusic(2f, wait = false)
    actionDialog("masamune_forest_00", withSound = false)
    actionFacing(triggeringEntity, FacingDirection.UP)

    // fire demon
    actionEnableInput(false)
    val spawnDemonFire = tiledService.loadPoint("spawn_demon_1")
    actionDelay(1.5f)
    actionPlaySound(SoundAsset.DEMON_TELEPORT)
    actionSpawnEntity(TiledObjectType.DEMON_FIRE, spawnDemonFire)
    val demonFire = selectEntity { tiledFamily.single { it[Tiled].objType == TiledObjectType.DEMON_FIRE } }
    actionEntitySpeed(demonFire, 2f)
    actionFollowPath(demonFire, 39, removeAtEnd = true, waitForEnd = true)
    actionEnableInput(true)
    actionDialog("masamune_forest_10", withSound = false)

    // spirit demon
    actionEnableInput(false)
    actionDelay(1.5f)
    val spawnDemonSpirit = tiledService.loadPoint("spawn_demon_2")
    actionPlaySound(SoundAsset.DEMON_TELEPORT)
    actionSpawnSfx("portal_white", spawnDemonSpirit - vec2(1.6f, 1.6f), 1f, 0.75f)
    actionDelay(0.3f)
    actionSpawnEntity(TiledObjectType.DEMON_SPIRIT, spawnDemonSpirit)
    val demonSpirit = selectEntity { tiledFamily.single { it[Tiled].objType == TiledObjectType.DEMON_SPIRIT } }
    actionEntitySpeed(demonSpirit, 1.5f)
    actionFollowPath(demonSpirit, 40, removeAtEnd = true, waitForEnd = true)
    actionEnableInput(true)
    actionDialog("masamune_forest_20", withSound = false)

    // cyclops
    actionEnableInput(false)
    actionDelay(1.5f)
    val spawnCyclops = tiledService.loadPoint("spawn_cyclops")
    actionPlaySound(SoundAsset.DEMON_TELEPORT)
    actionSpawnSfx("skull_green", spawnCyclops - vec2(1.6f, 1.6f), 1f, 0.75f)
    actionDelay(0.3f)
    actionSpawnEntity(TiledObjectType.CYCLOPS_NPC, spawnCyclops)
    actionDelay(1.5f)

    // demons removal
    actionEnableInput(true)
    actionDialog("masamune_forest_30", withSound = false)
    actionEnableInput(false)
    actionDelay(1.5f)
    val despawnDemonFire = tiledService.loadPoint("despawn_demon_1")
    actionPlaySound(SoundAsset.DEMON_TELEPORT)
    actionSpawnSfx("portal_red", despawnDemonFire - vec2(2f, 1.6f), 1f, 0.75f)
    actionDelay(0.3f)
    actionRemove(demonFire)
    actionDelay(0.5f)
    val despawnDemonSpirit = tiledService.loadPoint("despawn_demon_2")
    actionPlaySound(SoundAsset.DEMON_TELEPORT)
    actionSpawnSfx("portal_white", despawnDemonSpirit - vec2(2f, 1.6f), 1f, 0.75f)
    actionDelay(0.3f)
    actionRemove(demonSpirit)

    // start boss combat
    actionDelay(1.2f)
    val cyclops = selectEntity { tiledFamily.single { it[Tiled].objType == TiledObjectType.CYCLOPS_NPC } }
    actionEnableInput(true)
    val combatEnemies = mapOf(TiledObjectType.CYCLOPS to 1)
    actionStartCombat(triggeringEntity, cyclops, MusicAsset.COMBAT2, combatEnemies) { victory ->
        if (!victory) {
            actionDialog("combat_defeat", withSound = true) {
                this@masamuneForestTrigger.inject<Masamune>().transitionScreen<MainMenuScreen>(
                    FadeTransitionType(1f, 1f, 0.1f),
                    FadeTransitionType(1f, 1f, 0.1f, delayInSeconds = 0.1f),
                )
            }
        } else {
            actionDialog("masamune_forest_40", withSound = true)
        }
    }
}
