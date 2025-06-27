package io.github.masamune.component

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.World
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

class MoveTo(
    val to: Vector2,
    time: Float,
    val interpolation: Interpolation,
) : Component<MoveTo> {

    var alpha: Float = 1f
    var speed = if (time == 0f) 0f else (1f / time)
    val from = vec2()

    override fun type() = MoveTo

    override fun World.onAdd(entity: Entity) {
        val (fromX, fromY) = entity[Transform].position
        from.set(fromX, fromY)
    }

    companion object : ComponentType<MoveTo>()
}

fun EntityUpdateContext.teleport(entity: Entity, to: Vector2) {
    val moveToCmp = entity.getOrNull(MoveTo)
    if (moveToCmp != null) {
        moveToCmp.to.set(to)
        moveToCmp.speed = 0f
    } else {
        entity += MoveTo(to, 0f, Interpolation.fade)
    }
}
