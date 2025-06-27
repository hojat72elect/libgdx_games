package io.github.masamune.system

import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.masamune.PhysicWorld
import io.github.masamune.component.Move
import io.github.masamune.component.Physic
import io.github.masamune.component.Tag
import io.github.masamune.component.Transform
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

class PhysicSystem(
    private val physicWorld: PhysicWorld = inject(),
    interval: Interval = Fixed(1 / 60f),
) : IteratingSystem(family { all(Physic).any(Move, Transform) }, interval = interval) {

    override fun onUpdate() {
        if (physicWorld.autoClearForces) {
            // AutoClearForces must be false to guarantee a correct physic step behavior
            physicWorld.autoClearForces = false
        }
        super.onUpdate()
        physicWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        physicWorld.step(deltaTime, 6, 2)
    }

    override fun onTickEntity(entity: Entity) {
        val (body, prevPosition) = entity[Physic]
        prevPosition.set(body.position)

        entity.getOrNull(Move)?.let { (moveSpeed, direction) ->
            val (velX, velY) = body.linearVelocity
            val (dirX, dirY) = direction
            val speed = if (entity has Tag.PAUSE) 0f else moveSpeed

            TMP_IMPULSE.set(speed * dirX - velX, speed * dirY - velY)
            body.applyLinearImpulse(TMP_IMPULSE.scl(body.mass), body.worldCenter, true)
        }
    }

    // interpolate between position before world step and real position after world step for smooth rendering
    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val (position) = entity[Transform]
        val (body, prevPosition) = entity[Physic]

        val (prevX, prevY) = prevPosition
        val (bodyX, bodyY) = body.position
        position.set(
            MathUtils.lerp(prevX, bodyX, alpha),
            MathUtils.lerp(prevY, bodyY, alpha),
            position.z
        )
    }

    companion object {
        private val TMP_IMPULSE = vec2()
    }

}
