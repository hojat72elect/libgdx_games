package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService

class NullifyMagicBuff(override val owner: Entity, private var numMagic: Int) : OnMagicDamageTakenBuff {
    override fun ActionExecutorService.preMagicDamageTaken(source: Entity, target: Entity, damage: Float): Float = 0f

    override fun ActionExecutorService.postMagicDamageTaken(source: Entity, target: Entity, damage: Float) {
        play(SoundAsset.ATTACK_MISS)
        addSfx(owner, "shield-yellow", 0.5f, 1.5f)
        wait(0.75f)

        --numMagic
        if (numMagic <= 0) {
            removeBuff()
        }
    }
}
