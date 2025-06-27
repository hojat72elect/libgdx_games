package io.github.masamune.combat.effect

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

data class DelayEffect(
    override val source: Entity,
    override val target: Entity,
    private var time: Float,
) : Effect {

    override fun World.onUpdate(): Boolean {
        time -= deltaTime
        return time <= 0f
    }
}
