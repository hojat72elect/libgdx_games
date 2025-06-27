package io.github.masamune.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.EntityBag
import io.github.masamune.combat.ActionState
import io.github.masamune.component.StatusType
import io.github.masamune.dialog.Dialog
import io.github.masamune.tiledmap.ActionType
import io.github.masamune.tiledmap.MapTransitionType
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.MenuType

sealed interface Event

// GENERAL EVENTS
data class PlayerMoveEvent(val direction: Vector2) : Event
data class GameResizeEvent(val width: Int, val height: Int) : Event
data class PlayerQuestItemBegin(val player: Entity, val item: Entity) : Event
data object PlayerQuestItemEnd : Event
data object GameStartEvent : Event
data class CutSceneTextEvent(val i18NKey: I18NKey, val align: Int, val duration: Float) : Event
data object CutSceneAbortEvent : Event
data class SaveEvent(val world: World) : Event
data class LoadEvent(val world: World) : Event

// PLAYER INTERACT EVENTS
data class PlayerInteractBeginContactEvent(val player: Entity, val other: Entity) : Event
data class PlayerInteractEndContactEvent(val player: Entity, val other: Entity) : Event
data object PlayerInteractEvent : Event
data class PlayerInteractCombatBeginEvent(val world: World, val autoSave:Boolean) : Event
data class PlayerInteractCombatEndEvent(val victory: Boolean, val enemy: Entity) : Event

// MENU EVENTS
data class MenuBeginEvent(val type: MenuType) : Event
data object MenuEndEvent : Event

// DIALOG EVENTS
data class DialogBeginEvent(val world: World, val player: Entity, val dialog: Dialog, val withSound: Boolean) : Event
data class DialogEndEvent(val player: Entity, val dialog: Dialog, val optionIdx: Int) : Event

// UI EVENTS
data object UiUpEvent : Event
data object UiDownEvent : Event
data object UiRightEvent : Event
data object UiLeftEvent : Event
data object UiSelectEvent : Event
data object UiBackEvent : Event

// SHOP EVENTS
data class ShopBeginEvent(val world: World, val player: Entity, val shop: Entity) : Event
data object ShopEndEvent : Event

// MAP + TRANSITION EVENTS
data class MapChangeEvent(val tiledMap: TiledMap, val ignoreTrigger: Boolean, val world: World) : Event
data class BeforeMapChangeEvent(val tiledMap: TiledMap, val world: World) : Event
data class MapTransitionBeginEvent(
    val fromTiledMap: TiledMap,
    val toTiledMap: TiledMap,
    val time: Float,
    val interpolation: Interpolation,
    val type: MapTransitionType,
    val mapOffset: Vector2,
    val newPlayerPos: Vector2, // position in new map (=toTiledMap)
    val playerSize: Vector2, // position in new map (=toTiledMap)
    val world: World,
) : Event
data class MapTransitionAfterObjectLoadEvent(val toTiledMap: TiledMap, val world: World) : Event
data class MapTransitionEndEvent(val world: World) : Event

// COMBAT EVENTS
data class CombatStartEvent(val player: Entity, val enemies: EntityBag) : Event
data class CombatPlayerActionEvent(val player: Entity) : Event
data class CombatActionStartEvent(val entity: Entity, val actionType: ActionType) : Event
data object CombatActionsPerformedEvent : Event
data class CombatNextTurnEvent(val player: Entity, val enemies: EntityBag) : Event
data class CombatTurnBeginEvent(val turn: Int) : Event
data class CombatTurnSortedEvent(val entities: EntityBag) : Event
data object CombatTurnEndEvent : Event
data object CombatPlayerDefeatEvent : Event
data object CombatPlayerVictoryEvent : Event
data class CombatEntityDeadEvent(val entity: Entity) : Event
data class CombatEntityTakeDamageEvent(
    val entity: Entity,
    val amount: Float,
    val life: Float,
    val maxLife: Float,
    val critical: Boolean
) : Event
data class CombatEntityHealEvent(val entity: Entity, val amount: Float, val life: Float, val maxLife: Float) : Event
data class CombatEntityManaUpdateEvent(
    val entity: Entity,
    val amount: Float,
    val mana: Float,
    val maxMana: Float,
    val state: ActionState,
) : Event
data class CombatMissEvent(val entity: Entity) : Event
data class CombatEntityTransformEvent(
    val originalEntity: Entity,
    val newEntity: Entity,
    val life: Float,
    val maxLife: Float
) : Event
data class CombatPlayerBuffAddEvent(val type: StatusType) : Event
data class CombatPlayerBuffRemoveEvent(val type: StatusType) : Event
