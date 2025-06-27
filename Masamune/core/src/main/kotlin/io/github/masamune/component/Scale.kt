package io.github.masamune.component

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Scale(
    val interpolation: Interpolation,
    val from: Float,
    val by: Float,
    val speed: Float,
    var alpha: Float = 0f,
) : Component<Scale> {
    override fun type() = Scale

    companion object : ComponentType<Scale>()
}
