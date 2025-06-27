package io.github.masamune.combat.effect

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.CharacterStats

data class DamageEffect(
    override val source: Entity,
    override val target: Entity,
    val damage: Float,
    val critical: Boolean,
) : Effect {
    var targetLife: Float = 0f
    var targetLifeMax: Float = 0f

    override fun World.onStart() {
        val targetStats = target[CharacterStats]
        targetStats.life = (targetStats.life - damage).coerceIn(0f, targetStats.lifeMax)

        targetLife = targetStats.life
        targetLifeMax = targetStats.lifeMax
    }
}
