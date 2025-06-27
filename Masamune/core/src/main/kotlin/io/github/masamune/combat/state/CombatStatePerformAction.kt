package io.github.masamune.combat.state

import com.github.quillraven.fleks.World
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.action.DefaultAction
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import io.github.masamune.isEntityDead
import ktx.log.logger

class CombatStatePerformAction(
    private val world: World,
    private val actionExecutorService: ActionExecutorService = world.inject(),
) : CombatState {
    private val combatEntities = world.family { all(Combat) }
    private var turnEnd = false

    override fun onEnter() {
        turnEnd = false
        combatEntities.forEach { entity ->
            if (world.isEntityDead(entity)) {
                return@forEach
            }

            val (_, action, targets) = entity[Combat]
            if (action == DefaultAction) {
                // this can happen for enemies if they are e.g. stunned and cannot
                // execute any action -> do nothing
                return@forEach
            }
            actionExecutorService.performOnTurnBeginBuffs(entity)
            actionExecutorService.queueAction(entity, action, targets)
        }
        actionExecutorService.performFirst()
    }

    private fun debugCombatEntities() = buildString {
        append("Combat Stats debug:")
        appendLine()
        combatEntities.forEach { entity ->
            append("Entity ${entity.id} (")
            append(if (entity has Player) "Player" else "Enemy")
            append("): ")
            val stats = entity[CharacterStats]
            val buffs = entity[Combat].buffs.joinToString { "${it::class.simpleName}" }
            append("life=${stats.life}, mana=${stats.mana}, agi=${stats.agility}, buffs=$buffs")
            appendLine()
        }
    }

    override fun onUpdate(deltaTime: Float) {
        actionExecutorService.update()
    }

    fun onTurnEnd() {
        combatEntities.forEach { entity ->
            if (world.isEntityDead(entity)) {
                return@forEach
            }
            actionExecutorService.performOnTurnEndBuffs(entity)
        }
    }

    override fun onExit() {
        log.debug { debugCombatEntities() }
    }

    companion object {
        private val log = logger<CombatStatePerformAction>()
    }
}
