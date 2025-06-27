package io.github.masamune.combat.action

import com.github.quillraven.fleks.Entity
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

enum class ActionTargetType {
    NONE, SINGLE, MULTI, ALL
}

sealed class Action(
    val type: ActionType,
    val targetType: ActionTargetType,
    val manaCost: Int = 0,
    val defensive: Boolean = false,
) {
    open fun ActionExecutorService.canPerform(entity: Entity): Boolean = hasMana(entity, manaCost)
    open fun ActionExecutorService.onCombatStart() = Unit
    open fun ActionExecutorService.onStart() = Unit
    open fun ActionExecutorService.onUpdate(): Boolean = true
    open fun ActionExecutorService.onFinish() = Unit
}

data object DefaultAction : Action(ActionType.UNDEFINED, ActionTargetType.NONE, manaCost = 0)

val Action.isHealing: Boolean
    get() = this is HealAction

val Action.isPoison: Boolean
    get() = this is PoisonAttackAction
