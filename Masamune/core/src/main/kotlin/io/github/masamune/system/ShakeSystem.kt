package io.github.masamune.system

import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Shake
import io.github.masamune.component.Transform
import io.github.masamune.event.CombatEntityTakeDamageEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener

class ShakeSystem : IteratingSystem(family { all(Shake, Transform) }), EventListener {

    override fun onTickEntity(entity: Entity): Unit = with(entity[Shake]) {
        val power = max * ((duration - currentDuration) / duration)
        currentDuration += deltaTime
        if (currentDuration >= duration) {
            entity[Transform].offset.set(0f, 0f)
            entity.configure { it -= Shake }
        } else {
            entity[Transform].offset.set(MathUtils.random(-1f, 1f) * power, MathUtils.random(-1f, 1f) * power)
        }
    }

    override fun onEvent(event: Event) {
        if (event is CombatEntityTakeDamageEvent) {
            event.entity.configure {
                it += Shake(max = 0.25f, duration = 0.5f)
            }
        }
    }
}
