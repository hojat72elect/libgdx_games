package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService

class ReflectAttackBuff(
    override val owner: Entity,
    private var numAttacks: Int,
    private val reflectPercentage: Float,
) : OnAttackDamageTakenBuff {
    override fun ActionExecutorService.postAttackDamageTaken(source: Entity, target: Entity, damage: Float) {
        --numAttacks
        val reflectDamage = damage * reflectPercentage
        dealMagicDamage(owner, reflectDamage, source, "shield-yellow", 0.5f, 1.5f, SoundAsset.SWORD_SWIPE, 1f)
        if (numAttacks <= 0f) {
            removeBuff()
        }
    }
}
