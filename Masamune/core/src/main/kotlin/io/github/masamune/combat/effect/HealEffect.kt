package io.github.masamune.combat.effect

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.CharacterStats

data class HealEffect(
    override val source: Entity,
    override val target: Entity,
    val amountLife: Float,
    val amountMana: Float,
) : Effect {
    var targetLife = 0f
    var targetLifeMax = 0f
    var targetMana = 0f
    var targetManaMax = 0f

    override fun World.onStart() {
        val targetStats = target[CharacterStats]
        targetStats.life = (targetStats.life + amountLife).coerceIn(0f, targetStats.lifeMax)
        targetStats.mana = (targetStats.mana + amountMana).coerceIn(0f, targetStats.manaMax)
        targetLife = targetStats.life
        targetLifeMax = targetStats.lifeMax
        targetMana = targetStats.mana
        targetManaMax = targetStats.manaMax
    }
}
