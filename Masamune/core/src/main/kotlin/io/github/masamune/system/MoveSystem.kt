package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Move
import io.github.masamune.component.Player
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.PlayerMoveEvent

class MoveSystem : IteratingSystem(enabled = false, family = family { all(Move) }), EventListener {

    private val playerEntities = family { all(Move, Player) }

    override fun onTickEntity(entity: Entity) = Unit

    override fun onEvent(event: Event) {
        when (event) {
            is PlayerMoveEvent -> playerEntities.forEach {
                // normalize direction to avoid moving faster in diagonal directions
                it[Move].direction.set(event.direction).nor()
            }

            else -> Unit
        }
    }

}
