package com.bitfire.uracer.game.task

import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.events.TaskManagerEvent
import com.bitfire.uracer.game.events.TaskManagerEvent.Order
import com.bitfire.uracer.game.events.TaskManagerEvent.Type


abstract class Task(private val order: Order) : TaskManagerEvent.Listener {

    @JvmField
    protected var isPaused = false

    init {
        GameEvents.taskManager.addListener(this, Type.OnTick, order)
        GameEvents.taskManager.addListener(this, Type.OnTickCompleted, order)
        GameEvents.taskManager.addListener(this, Type.OnPause, order)
        GameEvents.taskManager.addListener(this, Type.OnResume, order)
    }

    open fun dispose() {
        GameEvents.taskManager.removeListener(this, Type.OnTick, order)
        GameEvents.taskManager.removeListener(this, Type.OnTickCompleted, order)
        GameEvents.taskManager.removeListener(this, Type.OnPause, order)
        GameEvents.taskManager.removeListener(this, Type.OnResume, order)
    }

    protected abstract fun onTick()

    protected open fun onTickCompleted() {}

    protected open fun onGamePause() { isPaused = true }

    protected open fun onGameResume() { isPaused = false }

    override fun handle(source: Any, type: Type, order: Order) {
        when (type) {
            Type.OnTick -> if (!isPaused) onTick()
            Type.OnTickCompleted -> if (!isPaused) onTickCompleted()
            Type.OnPause -> onGamePause()
            Type.OnResume -> onGameResume()
        }
    }
}
