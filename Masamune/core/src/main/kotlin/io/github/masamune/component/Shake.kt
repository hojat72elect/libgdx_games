package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Shake(
    val max: Float, // max shake offset in world units
    val duration: Float, // duration in seconds
) : Component<Shake> {
    var currentDuration = 0f

    override fun type() = Shake

    companion object : ComponentType<Shake>()
}
