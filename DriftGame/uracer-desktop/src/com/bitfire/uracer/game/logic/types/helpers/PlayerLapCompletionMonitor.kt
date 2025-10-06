package com.bitfire.uracer.game.logic.types.helpers

import com.badlogic.gdx.math.MathUtils
import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.events.PlayerLapCompletionMonitorEvent
import com.bitfire.uracer.game.logic.helpers.GameTrack

open class PlayerLapCompletionMonitor(protected var gameTrack: GameTrack) {

    protected var prev = 0F
    protected var completion = 0F
    private var wuPrev = 0F
    var warmUpCompletion = 0F
    private var warmUpStartedCalled = false
    open var isWarmUp = false

    init {
        reset()
    }

    @JvmOverloads
    open fun reset(warmUp: Boolean = true) {
        prev = 0F
        completion = -1F
        warmUpCompletion = 0F
        wuPrev = 0F
        warmUpStartedCalled = false
        isWarmUp = warmUp
    }

    open fun update(car: Car) {
        val state = car.trackState

        if (isWarmUp) {
            if (!warmUpStartedCalled) {
                warmUpStartedCalled = true
                GameEvents.lapCompletion.trigger(car, PlayerLapCompletionMonitorEvent.Type.OnWarmUpStarted)
            }

            wuPrev = MathUtils.clamp(this.warmUpCompletion, 0f, 1f)
            val complet = gameTrack.getTrackCompletion(car)
            val start = state.initialCompletion
            this.warmUpCompletion = MathUtils.clamp((complet - start) / (1 - start), 0f, 1f)

            if (hasFinished(wuPrev, this.warmUpCompletion)) {
                this.warmUpCompletion = 1f
                isWarmUp = false
                GameEvents.lapCompletion.trigger(car, PlayerLapCompletionMonitorEvent.Type.OnWarmUpCompleted)
                GameEvents.lapCompletion.trigger(car, PlayerLapCompletionMonitorEvent.Type.OnLapStarted)
            }
        } else {
            prev = completion
            completion = gameTrack.getTrackCompletion(car)
            if (hasFinished(prev, completion)) {
                GameEvents.lapCompletion.trigger(car, PlayerLapCompletionMonitorEvent.Type.OnLapCompleted)
                GameEvents.lapCompletion.trigger(car, PlayerLapCompletionMonitorEvent.Type.OnLapStarted)
            }
        }
    }

    protected fun hasFinished(prev: Float, curr: Float) = prev > 0.9F && curr >= 0 && curr < 0.1F

}
