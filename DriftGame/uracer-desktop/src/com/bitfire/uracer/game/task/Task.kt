package com.bitfire.uracer.game.task

import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.events.TaskManagerEvent.*

abstract class Task(private val order: Order) : Listener {

    @JvmField
    protected var isPaused = false

    init {
        GameEvents.taskManager.addListener(this, Type.onTick, order)
        GameEvents.taskManager.addListener(this, Type.onTickCompleted, order)
        GameEvents.taskManager.addListener(this, Type.onPause, order)
        GameEvents.taskManager.addListener(this, Type.onResume, order)
    }

    open fun dispose() {
        GameEvents.taskManager.removeListener(this, Type.onTick, order)
        GameEvents.taskManager.removeListener(this, Type.onTickCompleted, order)
        GameEvents.taskManager.removeListener(this, Type.onPause, order)
        GameEvents.taskManager.removeListener(this, Type.onResume, order)
    }

    protected abstract fun onTick()

    protected open fun onTickCompleted() {}

    protected open fun onGamePause() { isPaused = true }

    protected open fun onGameResume() { isPaused = false }

    override fun handle(source: Any?, type: Type, order: Order) {
        when (type) {
            Type.onTick -> if (!isPaused) onTick()
            Type.onTickCompleted -> if (!isPaused) onTickCompleted()
            Type.onPause -> onGamePause()
            Type.onResume -> onGameResume()
        }
    }
}
