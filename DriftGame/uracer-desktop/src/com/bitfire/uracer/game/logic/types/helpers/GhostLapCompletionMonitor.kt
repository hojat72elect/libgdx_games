package com.bitfire.uracer.game.logic.types.helpers

import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.events.GhostLapCompletionMonitorEvent
import com.bitfire.uracer.game.logic.helpers.GameTrack

class GhostLapCompletionMonitor(gameTrack: GameTrack) : PlayerLapCompletionMonitor(gameTrack) {

    override var isWarmUp = false

    override fun reset(warmUp: Boolean) {
        super.reset(false)
    }

    override fun update(car: Car) {
        val state = car.trackState
        prev = completion
        completion = gameTrack.getTrackCompletion(car)
        if (hasFinished(prev, completion)) {
            state.ghostArrived = true
            GameEvents.ghostLapCompletion.trigger(car, GhostLapCompletionMonitorEvent.Type.OnLapCompleted)
        }
    }
}
