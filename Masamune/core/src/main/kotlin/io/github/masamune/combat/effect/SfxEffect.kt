package io.github.masamune.combat.effect

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.spawnSfx

data class SfxEffect(
    override val source: Entity,
    override val target: Entity,
    private val sfxAtlasKey: String,
    private val duration: Float,
    private val scale: Float = 1f,
) : Effect {

    override fun World.onStart() = spawnSfx(target, sfxAtlasKey, duration, scale)
}
