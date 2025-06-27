package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.effect.DelayEffect
import io.github.masamune.combat.effect.Effect
import io.github.masamune.combat.effect.TransformEffect
import io.github.masamune.tiledmap.TiledObjectType

data class TransformBuff(
    override val owner: Entity,
    private val toType: TiledObjectType,
    private val spawnX: Float,
    private val spawnY: Float,
) : OnDeathBuff {

    override fun ActionExecutorService.onDeath(source: Entity, target: Entity): List<Effect> {
        val transformTime = 4f
        return listOf(
            TransformEffect(owner, toType, spawnX, spawnY, transformTime),
            DelayEffect(owner, owner, transformTime),
        )
    }

}
