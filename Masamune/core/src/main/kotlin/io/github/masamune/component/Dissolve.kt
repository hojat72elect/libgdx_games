package io.github.masamune.component

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

data class Dissolve(
    val speed: Float,
    val uvOffset: Vector2,
    val uvMax: Vector2,
    val numFragments: Vector2,
    var value: Float = 0f, // 0..1
) : Component<Dissolve> {

    override fun type() = Dissolve

    companion object : ComponentType<Dissolve>() {
        fun ofRegion(region: TextureRegion, speed: Float): Dissolve {
            // numFragments is equal to the amount of pixels of the sprite.
            // This gives a nice effect for our pixelated graphics.
            val numFragments = vec2(region.regionWidth.toFloat(), region.regionHeight.toFloat())
            val uvOffset = vec2(region.u, region.v)
            val uvMax = vec2(region.u2, region.v2)
            return Dissolve(speed, uvOffset, uvMax, numFragments)
        }
    }
}
