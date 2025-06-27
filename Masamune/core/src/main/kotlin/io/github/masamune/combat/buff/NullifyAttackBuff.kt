package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService

class NullifyAttackBuff(override val owner: Entity, private var numAttacks: Int) : OnAttackDamageTakenBuff {
    override fun ActionExecutorService.preAttackDamageTaken(source: Entity, target: Entity, damage: Float): Float = 0f

    override fun ActionExecutorService.postAttackDamageTaken(source: Entity, target: Entity, damage: Float) {
        play(SoundAsset.ATTACK_MISS)
        addSfx(owner, "shield-yellow", 0.5f, 1.5f)
        wait(0.75f)

        --numAttacks
        if (numAttacks <= 0) {
            removeBuff()
        }
    }
}
