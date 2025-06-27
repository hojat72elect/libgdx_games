package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.MoveTo
import io.github.masamune.component.Physic

class MoveToSystem : IteratingSystem(family { all(MoveTo, Physic) }) {

    override fun onTickEntity(entity: Entity) = with(entity[MoveTo]) {
        val (body, prevPosition) = entity[Physic]

        alpha = when (speed) {
            // move instantly to target location
            0f -> 0f
            // interpolate to target location
            else -> (alpha - deltaTime * speed).coerceAtLeast(0f)
        }
        val invAlpha = 1f - alpha
        val newX = interpolation.apply(from.x, to.x, invAlpha)
        val newY = interpolation.apply(from.y, to.y, invAlpha)
        body.setTransform(newX, newY, body.angle)
        prevPosition.set(newX, newY)

        if (alpha == 0f) {
            // moveTo is finished -> remove it
            entity.configure {
                it -= MoveTo
            }
        }
    }

}
