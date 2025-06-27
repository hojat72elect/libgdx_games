package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Facing
import io.github.masamune.component.Player
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.PlayerMoveEvent

class FacingSystem : IteratingSystem(family { all(Facing) }), EventListener {
    private val playerEntities = family { all(Facing, Player) }

    override fun onTickEntity(entity: Entity) = with(entity[Facing]) {
        lastDirection = direction
    }

    override fun onEvent(event: Event) {
        if (event is PlayerMoveEvent) {
            playerEntities.forEach { entity ->
                entity[Facing].setByDirection(event.direction)
            }
        }
    }
}
