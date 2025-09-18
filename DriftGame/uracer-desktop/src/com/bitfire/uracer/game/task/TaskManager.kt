package com.bitfire.uracer.game.task

import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.events.TaskManagerEvent

class TaskManager() {
    fun dispose() {
        GameEvents.taskManager.removeAllListeners()
    }

    fun dispatchEvent(eventType: TaskManagerEvent.Type) {
        GameEvents.taskManager.trigger(this, eventType)
    }
}
