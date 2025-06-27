package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.effect.Effect

sealed interface Buff {
    val owner: Entity

    fun ActionExecutorService.onApply() = Unit
    fun ActionExecutorService.onRemove() = Unit
}

sealed interface OnAttackDamageBuff : Buff {
    fun ActionExecutorService.preAttackDamage(source: Entity, target: Entity, damage: Float): Float = damage
    fun ActionExecutorService.postAttackDamage(source: Entity, target: Entity, damage: Float) = Unit
}

sealed interface OnAttackDamageTakenBuff : Buff {
    fun ActionExecutorService.preAttackDamageTaken(source: Entity, target: Entity, damage: Float): Float = damage
    fun ActionExecutorService.postAttackDamageTaken(source: Entity, target: Entity, damage: Float) = Unit
}

sealed interface OnMagicDamageBuff : Buff {
    fun ActionExecutorService.preMagicDamage(source: Entity, target: Entity, damage: Float): Float = damage
    fun ActionExecutorService.postMagicDamage(source: Entity, target: Entity, damage: Float) = Unit
}

sealed interface OnMagicDamageTakenBuff : Buff {
    fun ActionExecutorService.preMagicDamageTaken(source: Entity, target: Entity, damage: Float): Float = damage
    fun ActionExecutorService.postMagicDamageTaken(source: Entity, target: Entity, damage: Float) = Unit
}

sealed interface OnDeathBuff : Buff {
    fun ActionExecutorService.onDeath(source: Entity, target: Entity): List<Effect>
}

sealed interface OnTurnBuff : Buff {
    fun ActionExecutorService.onTurnBegin() = Unit
    fun ActionExecutorService.onTurnEnd() = Unit
}
