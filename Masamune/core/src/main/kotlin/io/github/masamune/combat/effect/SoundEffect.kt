package io.github.masamune.combat.effect

import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.asset.SoundAsset
import io.github.masamune.audio.AudioService

data class SoundEffect(
    override val source: Entity,
    override val target: Entity,
    val soundAsset: SoundAsset,
) : Effect {

    override fun World.onStart() {
        inject<AudioService>().play(soundAsset, pitch = MathUtils.random(0.7f, 1.3f))
    }
}
