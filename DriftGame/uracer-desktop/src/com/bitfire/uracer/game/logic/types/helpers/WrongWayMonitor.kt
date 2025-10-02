package com.bitfire.uracer.game.logic.types.helpers

import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.GameplaySettings
import com.bitfire.uracer.game.Time
import com.bitfire.uracer.game.events.WrongWayMonitorEvent

class WrongWayMonitor {
    private val wrongWayTimer = Time()
    var isWrongWay = false

    init {
        reset()
    }

    fun reset() {
        isWrongWay = false
        wrongWayTimer.reset()
    }

    fun update(trackRouteConfidence: Float) {
        if (trackRouteConfidence <= 0) {

            /*
            * player is on the wrong direction. if not already marked as on the wrong direction, check if confidence stays negative
            *  for more than <n> seconds after staying positive, then it's safe to assume the wrong way is being played.
            */
            if (!isWrongWay) {
                // try mark wrong way
                if (wrongWayTimer.isStopped)
                    wrongWayTimer.start()
                 else if (wrongWayTimer.elapsed().tickSeconds > GameplaySettings.MAX_SECONDS_WRONG_WAY_DETECTOR) {
                    wrongWayTimer.reset()
                    isWrongWay = true
                    GameEvents.wrongWay.trigger(null, WrongWayMonitorEvent.Type.OnWrongWayBegins)
                }
            }
        } else if (trackRouteConfidence > 0 && !isWrongWay) {
            // player changed his mind earlier and there weren't enough seconds of wrong way to mark it as that, reset the timer.
            if (!wrongWayTimer.isStopped) wrongWayTimer.reset()
        }
    }
}
