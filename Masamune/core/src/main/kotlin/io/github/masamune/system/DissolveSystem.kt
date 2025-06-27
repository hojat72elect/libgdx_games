package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Dissolve
import io.github.masamune.component.Graphic
import io.github.masamune.component.Player
import io.github.masamune.event.CombatEntityDeadEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener

class DissolveSystem : IteratingSystem(family { all(Dissolve) }), EventListener {

    override fun onTickEntity(entity: Entity) = with(entity[Dissolve]) {
        value = (value + speed * deltaTime).coerceAtMost(1f)
    }

    override fun onEvent(event: Event) {
        when {
            event is CombatEntityDeadEvent && event.entity hasNo Player -> {
                event.entity.configure {
                    it += Dissolve.ofRegion(it[Graphic].region, 0.65f)
                }
            }
        }
    }
}
