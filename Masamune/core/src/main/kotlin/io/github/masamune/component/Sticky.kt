package io.github.masamune.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Sticky(val target: Entity, val offset: Vector2) : Component<Sticky> {
    override fun type() = Sticky

    companion object : ComponentType<Sticky>()
}
