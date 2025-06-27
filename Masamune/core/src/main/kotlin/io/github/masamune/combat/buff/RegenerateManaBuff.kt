package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService

class RegenerateManaBuff(
    override val owner: Entity,
    private val amount: Float,
) : OnTurnBuff {

    override fun ActionExecutorService.onTurnEnd() {
        if (owner.stats.mana < owner.stats.manaMax) {
            heal(0f, amount, owner, "restore_blue", 1f, 1.5f, SoundAsset.HEAL1, 1.5f)
        }
    }

}
