package io.github.masamune.component

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class Grayscale(
    val initWeight: Float,
    val finalWeight: Float,
    time: Float, // in seconds
    val interpolation: Interpolation,
) : Component<Grayscale> {

    var alpha: Float = 0f
    val speed = 1f / time

    override fun type() = Grayscale

    companion object : ComponentType<Grayscale>()
}
