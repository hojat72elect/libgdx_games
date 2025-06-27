package io.github.masamune.combat.state

import com.github.quillraven.fleks.World
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import io.github.masamune.event.CombatPlayerDefeatEvent
import io.github.masamune.event.CombatPlayerVictoryEvent
import io.github.masamune.event.EventService
import io.github.masamune.isEntityDead
import ktx.log.logger

// global state
class CombatStateCheckVictoryDefeat(
    private val world: World,
    private val eventService: EventService = world.inject(),
    private val actionExecutorService: ActionExecutorService = world.inject(),
) : CombatState {
    private val enemyEntities = world.family { none(Player).all(Combat) }
    private val playerEntities = world.family { all(Player, Combat) }

    override fun onUpdate(deltaTime: Float) {
        if (actionExecutorService.hasEffects) {
            return
        }

        if (playerEntities.all { world.isEntityDead(it) }) {
            log.debug { "All player entities dead -> Defeat" }
            eventService.fire(CombatPlayerDefeatEvent)
        } else if (enemyEntities.all { world.isEntityDead(it) }) {
            log.debug { "All enemy entities dead -> Victory" }
            eventService.fire(CombatPlayerVictoryEvent)
        }
    }

    companion object {
        private val log = logger<CombatStateCheckVictoryDefeat>()
    }
}
