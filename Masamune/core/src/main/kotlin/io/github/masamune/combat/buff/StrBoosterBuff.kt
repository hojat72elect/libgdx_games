package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.combat.ActionExecutorService

class StrBoosterBuff(
    override val owner: Entity,
    private val factor: Float,
) : OnAttackDamageBuff {

    override fun ActionExecutorService.preAttackDamage(source: Entity, target: Entity, damage: Float): Float {
        return damage + source.stats.strength * factor
    }

}
