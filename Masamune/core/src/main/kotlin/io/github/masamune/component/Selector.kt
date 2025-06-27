package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Selector(
    var target: Entity,
    var confirmed: Boolean,
) : Component<Selector> {
    override fun type() = Selector

    companion object : ComponentType<Selector>()
}
