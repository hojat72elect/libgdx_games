package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.MoveBy
import io.github.masamune.component.Transform

class MoveBySystem : IteratingSystem(family { all(Transform, MoveBy) }) {

    override fun onTickEntity(entity: Entity): Unit = with(entity[MoveBy]) {
        alpha = (alpha + deltaTime * (1f / duration)).coerceAtMost(1f)
        val distX = interpolation.apply(0f, by.x, alpha)
        val distY = interpolation.apply(0f, by.y, alpha)
        val position = entity[Transform].position
        position.set(from.x + distX, from.y + distY, position.z)
        if (alpha >= 1f) {
            entity.configure { it -= MoveBy }
        }
    }

}
