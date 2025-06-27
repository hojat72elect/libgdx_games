package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Trigger(
    val name: String,
    var triggeringEntity: Entity = Entity.NONE,
) : Component<Trigger> {
    override fun type() = Trigger

    companion object : ComponentType<Trigger>()
}
