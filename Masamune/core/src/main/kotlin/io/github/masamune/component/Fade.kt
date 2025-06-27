package io.github.masamune.component

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class Fade(
    val from: Float,
    val to: Float,
    time: Float,
    val interpolation: Interpolation,
) : Component<Fade> {

    var alpha: Float = 1f
    val speed = 1f / time

    override fun type() = Fade

    companion object : ComponentType<Fade>()
}
