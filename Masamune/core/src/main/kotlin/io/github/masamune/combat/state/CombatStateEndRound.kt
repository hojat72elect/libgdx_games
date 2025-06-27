package io.github.masamune.combat.state

import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.EntityComparator
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import io.github.masamune.event.CombatNextTurnEvent
import io.github.masamune.event.CombatTurnSortedEvent
import io.github.masamune.event.EventService
import io.github.masamune.isEntityAlive

class CombatStateEndRound(
    private val world: World,
    private val combatComparator: EntityComparator,
    private val eventService: EventService = world.inject(),
) : CombatState {
    private var turnEndDelay = 0f
    private val combatEntities = world.family { all(Combat) }

    override fun onEnter() {
        turnEndDelay = 1f
    }

    override fun onUpdate(deltaTime: Float) {
        turnEndDelay -= deltaTime
        if (turnEndDelay <= 0f) {
            with(world) {
                val player = combatEntities.single { it has Player }
                val aliveEnemies = combatEntities.filter { it hasNo Player && isEntityAlive(it) }
                eventService.fire(CombatNextTurnEvent(player, aliveEnemies))
                combatEntities.sort(combatComparator)
                eventService.fire(CombatTurnSortedEvent(combatEntities.entities))
            }
        }
    }
}
