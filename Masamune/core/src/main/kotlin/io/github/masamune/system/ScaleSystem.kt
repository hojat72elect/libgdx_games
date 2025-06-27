package io.github.masamune.system

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Player
import io.github.masamune.component.Scale
import io.github.masamune.component.Transform
import io.github.masamune.event.CombatEntityDeadEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener

class ScaleSystem : IteratingSystem(family { all(Scale, Transform) }), EventListener {

    override fun onTickEntity(entity: Entity) = with(entity[Scale]) {
        entity[Transform].scale = interpolation.apply(from, from + by, alpha)
        alpha = (alpha + deltaTime * speed).coerceAtMost(1f)

        if (alpha >= 1f) {
            entity.configure { it -= Scale }
        }
    }

    override fun onEvent(event: Event) {
        when {
            event is CombatEntityDeadEvent && event.entity hasNo Player -> {
                event.entity.configure {
                    it += Scale(Interpolation.circleOut, it[Transform].scale, 1.5f, 0.2f)
                }
            }
        }
    }

}
