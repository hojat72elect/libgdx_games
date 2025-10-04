package com.bitfire.uracer.game.logic.helpers

import com.bitfire.uracer.game.actors.GhostCar
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.utils.AlgebraMath.fixup
import com.bitfire.uracer.utils.InterpolatedFloat

class TrackProgressData {

    @JvmField
    var playerToTarget = 0F

    @JvmField
    var isCurrentLapValid = false

    @JvmField
    var isWarmUp = false

    @JvmField
    var hasTarget = false

    @JvmField
    var targetArrived = false

    @JvmField
    var playerDistance = InterpolatedFloat()

    @JvmField
    var targetDistance = InterpolatedFloat()

    @JvmField
    var playerProgress = InterpolatedFloat()
    var playerProgressAdv = InterpolatedFloat()
    var targetProgress = InterpolatedFloat()

    init {
        reset(true)
        resetLogicStates()
    }

    fun reset(resetState: Boolean) {
        playerDistance.reset(0F, resetState)
        targetDistance.reset(0F, resetState)
        playerProgress.reset(0F, resetState)
        playerProgressAdv.reset(0F, resetState)
        targetProgress.reset(0F, resetState)
    }

    fun resetLogicStates() {
        isCurrentLapValid = true
        isWarmUp = true
        targetArrived = false
        playerToTarget = 0F
    }

    fun update(isWarmUp: Boolean, isCurrentLapValid: Boolean, gameTrack: GameTrack, player: PlayerCar, target: GhostCar?) {
        this.isCurrentLapValid = isCurrentLapValid
        this.isWarmUp = isWarmUp
        hasTarget = (target != null)
        targetArrived = (hasTarget && target!!.trackState.ghostArrived)
        playerToTarget = if (isCurrentLapValid) 0F else -1F

        if (isWarmUp) {
            reset(true)
        } else {
            if (isCurrentLapValid) {
                playerProgress.set(gameTrack.getTrackCompletion(player), SMOOTHING)
                playerDistance.set(gameTrack.getTrackDistance(player, 0F), SMOOTHING)

                if (hasTarget) {
                    playerProgressAdv.set(gameTrack.getTrackCompletion(player), SMOOTHING)
                    if (target!!.trackState.ghostArrived) {
                        playerToTarget = fixup(playerProgressAdv.get() - 1)
                    } else {
                        targetDistance.set(gameTrack.getTrackDistance(target, 0f), SMOOTHING)
                        targetProgress.set(gameTrack.getTrackCompletion(target), SMOOTHING)
                        playerToTarget = fixup(playerProgressAdv.get() - targetProgress.get())
                    }
                }
            } else {
                reset(true)
            }
        }
    }

    companion object {
        private const val SMOOTHING = 0.375F
    }
}
