package io.github.masamune.trigger

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.World
import io.github.masamune.Masamune
import io.github.masamune.asset.MusicAsset
import io.github.masamune.asset.SoundAsset
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.FacingDirection
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.EventService
import io.github.masamune.quest.Quest
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.tiledmap.TiledObjectType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.trigger.TriggerActionDialog.Companion.NO_CLOSE_ACTION
import io.github.masamune.trigger.actions.EntitySelector
import io.github.masamune.ui.model.I18NKey
import ktx.app.gdxError


@DslMarker
annotation class TriggerDsl

@TriggerDsl
class TriggerCfg(
    val world: World,
    val triggeringEntity: Entity,
    private val actions: MutableList<TriggerAction>,
) {
    fun actionRemove(entity: Entity) {
        actions += TriggerActionRemoveEntity(entity)
    }

    fun actionRemove(selector: EntitySelector) {
        actions += TriggerActionRemoveEntitySelector(selector)
    }

    fun actionDialog(
        dialogName: String,
        withSound: Boolean = true,
        closeAction: (selectedOptionIdx: Int) -> Unit = NO_CLOSE_ACTION
    ) {
        val configurator = world.inject<DialogConfigurator>()
        val eventService = world.inject<EventService>()
        actions += TriggerActionDialog(configurator, dialogName, withSound, world, triggeringEntity, eventService, closeAction)
    }

    fun actionAddItem(entity: Entity, itemType: ItemType, amount: Int = 1) {
        val tiledService = world.inject<TiledService>()
        val eventService = world.inject<EventService>()
        val audioService = world.inject<AudioService>()
        actions += TriggerActionAddItem(entity, itemType, amount, eventService, tiledService, audioService)
    }

    fun actionRemoveItem(entity: Entity, itemType: ItemType, amount: Int) {
        actions += TriggerActionRemoveItem(entity, itemType, amount)
    }

    fun actionAddQuest(entity: Entity, quest: Quest) {
        val eventService = world.inject<EventService>()
        actions += TriggerActionAddQuest(entity, quest, eventService)
    }

    fun actionCompleteQuest(quest: Quest) {
        val eventService = world.inject<EventService>()
        actions += TriggerActionCompleteQuest(quest, eventService)
    }

    fun actionMoveBack(entity: Entity, distance: Float, timeInSeconds: Float = 0f, wait: Boolean = true) {
        actions += TriggerActionMoveBack(entity, distance, timeInSeconds, wait)
    }

    fun actionShop(playerEntity: Entity, shopEntity: Entity, shopName: I18NKey, items: List<ItemType>) {
        val tiledService = world.inject<TiledService>()
        actions += TriggerActionShop(playerEntity, shopEntity, shopName, items, tiledService)
    }

    fun actionHeal(entity: Entity, healLife: Boolean, healMana: Boolean) {
        val audioService = world.inject<AudioService>()
        actions += TriggerActionHeal(entity, healLife, healMana, audioService)
    }

    fun actionPauseEntity(entity: Entity, pause: Boolean) {
        actions += TriggerActionPauseEntity(entity, pause)
    }

    fun actionPlayMusic(music: MusicAsset, loop: Boolean = true, keepPrevious: Boolean = false) {
        val audioService = world.inject<AudioService>()
        actions += TriggerActionPlayMusic(audioService, music, loop, keepPrevious)
    }

    fun actionPlaySound(sound: SoundAsset, pitch: Float = 1f) {
        val audioService = world.inject<AudioService>()
        actions += TriggerActionPlaySound(audioService, sound, pitch)
    }

    fun actionDelay(seconds: Float) {
        actions += TriggerActionDelay(seconds)
    }

    fun actionChangeScreen(action: Masamune.() -> Unit) {
        val masamune = world.inject<Masamune>()
        actions += TriggerActionChangeScreen(masamune, action)
    }

    fun actionCutSceneText(
        i18NKey: I18NKey,
        align: Int,
        duration: Float,
    ) {
        val eventService = world.inject<EventService>()
        actions += TriggerActionCutSceneText(i18NKey, align, duration, eventService)
        actionDelay(duration)
    }

    fun actionFadeOutMusic(duration: Float, wait: Boolean) {
        val audioService = world.inject<AudioService>()
        actions += TriggerActionFadeOutMusic(duration, audioService)
        if (wait) {
            actionDelay(duration)
        }
    }

    fun actionLoadMap(
        asset: TiledMapAsset,
        withBoundaries: Boolean = true,
        withTriggers: Boolean = true,
        withPortals: Boolean = true,
    ) {
        val tiledService = world.inject<TiledService>()
        actions += TriggerActionLoadMap(
            asset,
            tiledService,
            withBoundaries,
            withTriggers,
            withPortals,
        )
    }

    fun actionHideEntity(entitySelector: EntitySelector) {
        actions += TriggerActionHideEntity(entitySelector)
    }

    fun actionFollowPath(
        entitySelector: EntitySelector,
        pathId: Int,
        removeAtEnd: Boolean,
        waitForEnd: Boolean,
    ) {
        val tiledService = world.inject<TiledService>()
        actions += TriggerActionFollowPath(entitySelector, pathId, removeAtEnd, waitForEnd, tiledService)
    }

    fun selectEntity(selector: () -> Entity): EntitySelector {
        return EntitySelector(selector)
    }

    fun actionEntitySpeed(entitySelector: EntitySelector, speed: Float) {
        actions += TriggerActionEntitySpeed(entitySelector, speed)
    }

    fun actionEnableInput(enable: Boolean) {
        val processor = Gdx.input.inputProcessor
        actions += TriggerActionEnableInput(enable, processor)
    }

    fun actionSpawnEntity(type: TiledObjectType, location: Vector2) {
        val tiledService = world.inject<TiledService>()
        actions += TriggerActionSpawnEntity(type, location, tiledService)
    }

    fun actionSpawnSfx(sfxAtlasKey: String, location: Vector2, duration: Float, scaling: Float) {
        actions += TriggerActionSpawnSfx(sfxAtlasKey, location, duration, scaling)
    }

    fun actionStartCombat(
        player: Entity,
        enemy: EntitySelector,
        music: MusicAsset,
        enemies: Map<TiledObjectType, Int>,
        onCombatEnd: (Boolean) -> Unit
    ) {
        val masamune = world.inject<Masamune>()
        val eventService = world.inject<EventService>()
        actions += TriggerActionStartCombat(player, enemy, music, enemies, onCombatEnd, masamune, eventService)
    }

    fun actionConfigureEntity(entity: Entity, configuration: EntityUpdateContext.(Entity) -> Unit) {
        actions += TriggerActionConfigureEntity(entity, configuration)
    }

    fun actionFacing(entity: Entity, direction: FacingDirection) {
        actions += TriggerActionFacing(entity, direction)
    }

}

fun trigger(
    name: String,
    world: World,
    triggeringEntity: Entity,
    cfg: TriggerCfg.() -> Unit
): TriggerScript {
    val actions = mutableListOf<TriggerAction>()
    TriggerCfg(world, triggeringEntity, actions).apply(cfg)
    if (actions.isEmpty()) {
        gdxError("Trigger $name must have at least one action")
    }

    return TriggerScript(name, world, actions)
}
