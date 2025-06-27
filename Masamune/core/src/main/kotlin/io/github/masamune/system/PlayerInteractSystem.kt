package io.github.masamune.system

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.atan2Deg360
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.MutableEntityBag
import com.github.quillraven.fleks.collection.compareEntity
import io.github.masamune.Masamune
import io.github.masamune.PhysicContactHandler.Companion.testPoint
import io.github.masamune.asset.MusicAsset
import io.github.masamune.component.Dialog
import io.github.masamune.component.Dissolve
import io.github.masamune.component.Enemy
import io.github.masamune.component.Graphic
import io.github.masamune.component.Interact
import io.github.masamune.component.Outline
import io.github.masamune.component.Physic
import io.github.masamune.component.Player
import io.github.masamune.component.Portal
import io.github.masamune.component.Remove
import io.github.masamune.component.Scale
import io.github.masamune.component.Tag
import io.github.masamune.component.Transform
import io.github.masamune.component.Trigger
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.DialogBeginEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.EventService
import io.github.masamune.event.PlayerInteractBeginContactEvent
import io.github.masamune.event.PlayerInteractCombatBeginEvent
import io.github.masamune.event.PlayerInteractCombatEndEvent
import io.github.masamune.event.PlayerInteractEndContactEvent
import io.github.masamune.event.PlayerInteractEvent
import io.github.masamune.event.PlayerMoveEvent
import io.github.masamune.screen.BlurTransitionType
import io.github.masamune.screen.CombatScreen
import io.github.masamune.screen.DefaultTransitionType
import io.github.masamune.tiledmap.MapTransitionService
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import kotlin.math.abs

class PlayerInteractSystem(
    private val eventService: EventService = inject(),
    private val dialogConfigurator: DialogConfigurator = inject(),
    private val mapTransitionService: MapTransitionService = inject(),
    private val masamune: Masamune = inject(),
) : IteratingSystem(
    family = family { all(Interact, Player) },
    interval = Fixed(1 / 20f)
), EventListener {

    private val playerCenter = vec2()
    private val otherCenter = vec2()
    private val filteredDirectionEntities = MutableEntityBag(4)
    private val distanceComparator = compareEntity { e1, e2 -> (euclideanDist(e1).compareTo(euclideanDist(e2))) }

    override fun onTickEntity(entity: Entity) = with(entity[Interact]) {
        triggerTimer = (triggerTimer - deltaTime).coerceAtLeast(0f)

        if (nearbyEntities.isEmpty()) {
            // no entities to interact -> do nothing
            return@with
        }

        // playerCenter is used in handleMapTrigger and tagClosestEntity below
        entity[Transform].centerTo(playerCenter)
        // check for map trigger entities (=entities that are not rendered/visible to the player)
        if (handleMapTrigger(entity)) {
            return@with
        }

        // check for map portal entities
        if (handlePortals(entity)) {
            // player entered portal to a new map -> skip remaining system logic
            return@with
        }
        // tag the closest entity within direction with an OUTLINE tag to render it with an outline
        tagClosestEntity()

        if (triggerTimer == 0f) {
            // player did not press interact button yet -> do nothing
            return@with
        }

        triggerTimer = 0f
        onPlayerInteract(entity)
    }

    private fun Interact.onPlayerInteract(player: Entity) {
        when {
            interactEntity == Entity.NONE -> {
                // no entity to interact
                return
            }

            interactEntity has Dialog -> {
                val namedDialog = dialogConfigurator[interactEntity[Dialog].dialogName, world, player]
                eventService.fire(DialogBeginEvent(world, player, namedDialog, withSound = true))
            }

            interactEntity has Trigger -> {
                interactEntity.configure { it += Tag.EXECUTE_TRIGGER }
                interactEntity[Trigger].triggeringEntity = player
            }

            interactEntity has Enemy -> {
                eventService.fire(PlayerInteractCombatBeginEvent(world, autoSave = true))
                masamune.getScreen<CombatScreen>().playCombatMusic(MusicAsset.COMBAT1)
                masamune.transitionScreen<CombatScreen>(
                    fromType = BlurTransitionType(
                        startBlur = 0f,
                        endBlur = 6f,
                        startAlpha = 1f,
                        endAlpha = 0.4f,
                        time = 2f
                    ),
                    toType = DefaultTransitionType,
                ) { combatScreen ->
                    combatScreen.spawnEnemies(interactEntity, interactEntity[Enemy].combatEntities)
                    // call spawnPlayer method AFTER spawnEnemies because it fires an event
                    // that starts the combat, and we need the enemy entities at that point already.
                    combatScreen.spawnPlayer(world, player)
                }
            }
        }
    }

    private fun Entity.isPortal(): Boolean = this has Portal

    private fun Interact.handlePortals(player: Entity): Boolean {
        nearbyEntities.firstOrNull { it.isPortal() }?.let { portalEntity ->
            if (portalEntity[Physic].body.testPoint(playerCenter)) {
                // player is inside portal area -> start map transition
                mapTransitionService.startTransition(world, player, portalEntity)
                portalEntity.remove()
                return true
            }
        }

        return false
    }

    private fun Entity.isMapTrigger(): Boolean {
        // graphic check is needed because trigger can also be part of a NPC entity, and
        // in for those entities it needs to be triggered via player input
        return this has Trigger && this hasNo Graphic
    }

    private fun Interact.handleMapTrigger(player: Entity): Boolean {
        nearbyEntities.firstOrNull { it.isMapTrigger() }?.let { mapTriggerEntity ->
            if (mapTriggerEntity[Physic].body.testPoint(playerCenter)) {
                // player is inside trigger area -> execute trigger
                mapTriggerEntity[Trigger].triggeringEntity = player
                mapTriggerEntity.configure { it += Tag.EXECUTE_TRIGGER }
                return true
            }
        }

        return false
    }

    private fun Interact.tagClosestEntity() {
        // filter for closest entity in player direction
        filteredDirectionEntities.clear()
        nearbyEntities.filterTo(filteredDirectionEntities) { other ->
            !other.isMapTrigger() && !other.isPortal() && withinDirection(other, lastDirection)
        }
        filteredDirectionEntities.sort(distanceComparator)

        val closestEntity: Entity? = filteredDirectionEntities.firstOrNull()
        if (closestEntity != null && closestEntity != interactEntity) {
            val outlineColor = if (closestEntity has Enemy) Outline.COLOR_ENEMY else Outline.COLOR_NEUTRAL
            closestEntity.configure { it += Outline(outlineColor) }
            if (interactEntity != Entity.NONE) {
                interactEntity.configure { it -= Outline }
            }
            interactEntity = closestEntity
        } else if (interactEntity != Entity.NONE && interactEntity !in filteredDirectionEntities) {
            interactEntity.configure { it -= Outline }
            interactEntity = Entity.NONE
        }
    }

    private fun withinDirection(other: Entity, lastPlayerDirection: Vector2): Boolean {
        other[Transform].centerTo(otherCenter)
        val (dirX, dirY) = lastPlayerDirection

        // calculate angle between player and other entities [0..360]
        val angPlayerOther = atan2Deg360(otherCenter.y - playerCenter.y, otherCenter.x - playerCenter.x)
        // calculate player direction angle [0..360]
        val angPlayerDirection = when {
            dirY > 0f -> MathUtils.acosDeg(dirX)
            else -> 360f - MathUtils.acosDeg(dirX)
        }

        // check if other entity is within a cone of player facing
        // -> true when other entity is within a tolerance of INTERACT_RADIUS of player direction angle
        val difference = abs(angPlayerOther - angPlayerDirection)
        if (difference > 180) {
            // angles are on opposite sides of the circle
            // use circular difference instead of normal difference
            return 360 - difference <= INTERACT_ANG_TOLERANCE
        }

        // angles are on the same side of the circle
        return difference <= INTERACT_ANG_TOLERANCE
    }

    private fun euclideanDist(other: Entity): Float {
        other[Transform].centerTo(otherCenter)
        val diffX = otherCenter.x - playerCenter.x
        val diffY = otherCenter.y - playerCenter.y
        return diffX * diffX + diffY * diffY
    }

    private fun Entity.isNotInteractable(): Boolean {
        return this hasNo Dialog && this hasNo Trigger && this hasNo Portal && this hasNo Enemy
    }

    private fun onPlayerBeginInteract(player: Entity, other: Entity) {
        if (other.isNotInteractable()) {
            return
        }

        val (nearbyEntities) = player[Interact]
        nearbyEntities += other
        log.debug { "interact begin contact: ${nearbyEntities.size} entities" }
    }

    private fun onPlayerEndInteract(player: Entity, other: Entity) {
        // calling isNotInteractable will not work here because
        // a removed entity will trigger this event, but it won't have any components anymore
        // -> just execute the logic all the time
        val interactCmp = player[Interact]
        interactCmp.nearbyEntities -= other
        if (other has Outline) {
            other.configure { it -= Outline }
        }
        if (other == interactCmp.interactEntity) {
            interactCmp.interactEntity = Entity.NONE
        }
        log.debug { "interact end contact: ${interactCmp.nearbyEntities.size} entities" }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is PlayerMoveEvent -> {
                if (!event.direction.isZero) {
                    // only update direction when player is moving to keep the last direction
                    // even if the player stops moving
                    family.forEach { it[Interact].lastDirection.set(event.direction) }
                }
            }

            is PlayerInteractEvent -> family.forEach { it[Interact].triggerTimer = 0.1f }
            is PlayerInteractBeginContactEvent -> onPlayerBeginInteract(event.player, event.other)
            is PlayerInteractEndContactEvent -> onPlayerEndInteract(event.player, event.other)
            is PlayerInteractCombatEndEvent -> {
                if (event.victory) {
                    event.enemy.configure {
                        it += Scale(Interpolation.circleOut, it[Transform].scale, 2f, 0.3f)
                        it += Dissolve.ofRegion(it[Graphic].region, 1f)
                        it -= Physic
                        it += Remove(2f)
                    }
                }
            }
            else -> Unit
        }
    }

    companion object {
        private val log = logger<PlayerInteractSystem>()
        private const val INTERACT_ANG_TOLERANCE = 100f
    }
}
