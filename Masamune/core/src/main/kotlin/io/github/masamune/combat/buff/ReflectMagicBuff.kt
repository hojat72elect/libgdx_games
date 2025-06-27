package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService

data class ReflectMagicBuff(
    override val owner: Entity,
    private var numMagic: Int,
    private val reflectPercentage: Float,
) : OnMagicDamageTakenBuff {
    override fun ActionExecutorService.postMagicDamageTaken(source: Entity, target: Entity, damage: Float) {
        --numMagic
        val reflectDamage = damage * reflectPercentage
        dealMagicDamage(
            owner,
            reflectDamage,
            source,
            "fire1",
            0.5f,
            1.5f,
            SoundAsset.EXPLOSION1,
            1f,
            false,
        )
        if (numMagic <= 0f) {
            removeBuff()
        }
    }
}
