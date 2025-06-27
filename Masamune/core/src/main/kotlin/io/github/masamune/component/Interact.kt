package io.github.masamune.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.collection.MutableEntityBag
import ktx.math.vec2

data class Interact(
    val nearbyEntities: MutableEntityBag = MutableEntityBag(4),
    var triggerTimer: Float = 0f,
    val lastDirection: Vector2 = vec2(),
    var interactEntity: Entity = Entity.NONE,
) : Component<Interact> {
    override fun type() = Interact

    companion object : ComponentType<Interact>()
}
