package io.github.masamune.event

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.World
import io.github.masamune.ui.view.View

interface EventListener {
    fun onEvent(event: Event)
}

class EventService {

    private val listeners = mutableListOf<EventListener>()
    private val eventQueue = ArrayDeque<Event>(4)

    val numListeners: Int
        get() = listeners.size

    operator fun plusAssign(listener: EventListener) {
        if (listener in listeners) {
            return
        }

        listeners += listener
    }

    operator fun plusAssign(world: World) {
        world.systems
            .filterIsInstance<EventListener>()
            .forEach { this += it }
    }

    operator fun plusAssign(stage: Stage) {
        stage.actors
            .filterIsInstance<View<*>>()
            .forEach {
                this += it
                this += it.viewModel
            }
    }

    operator fun minusAssign(world: World) {
        world.systems
            .filterIsInstance<EventListener>()
            .forEach { this -= it }
    }

    operator fun minusAssign(listener: EventListener) {
        listeners -= listener
    }

    fun clearListeners() {
        listeners.clear()
    }

    operator fun contains(listener: EventListener): Boolean = listener in listeners

    fun fire(event: Event) {
        eventQueue.addLast(event)
        if (eventQueue.size > 1) {
            return
        }

        while (eventQueue.isNotEmpty()) {
            val eventToFire = eventQueue.first()
            listeners.forEach { it.onEvent(eventToFire) }
            eventQueue.removeFirst()
        }
    }

}
