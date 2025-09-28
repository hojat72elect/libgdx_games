package com.bitfire.uracer.game.player

import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.Time
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.events.PlayerDriftStateEvent
import com.bitfire.uracer.utils.AlgebraMath.clamp
import com.bitfire.uracer.utils.AlgebraMath.fixup
import com.bitfire.uracer.utils.AlgebraMath.lowpass
import kotlin.math.abs

class DriftState(private val car: Car) {

    @JvmField
    var isDrifting = false
    @JvmField
    var lateralForcesFront = 0F
    @JvmField
    var lateralForcesRear = 0F
    @JvmField
    var driftStrength = 0F
    private var hasCollided = false
    private val invMaxGrip = car.carModel.inv_max_grip
    private var lastRear = 0F
    private var lastFront = 0F
    private val time = Time()
    private val collisionTime = Time()

    init {
        reset()
    }

    fun dispose() {
        time.dispose()
        collisionTime.dispose()
        GameEvents.driftState.removeAllListeners()
    }

    fun reset() {
        time.reset()
        collisionTime.reset()
        lastFront = 0F
        lastRear = 0F
        hasCollided = false
        isDrifting = false
        lateralForcesFront = 0F
        lateralForcesRear = 0F
        driftStrength = 0F
    }

    fun invalidateByCollision() {
        if (!isDrifting) return

        isDrifting = false
        hasCollided = true
        collisionTime.start()
        time.stop()
        GameEvents.driftState.trigger(car, PlayerDriftStateEvent.Type.OnEndDrift)
    }

    fun update(latForceFrontY: Float, latForceRearY: Float, velocityLength: Float) {

        // lateral forces are in this range : [-max_grip, max_grip]
        lateralForcesFront = lowpass(lastFront, latForceFrontY, 0.2F)
        lastFront = lateralForcesFront
        lateralForcesFront = clamp(abs(lateralForcesFront) * invMaxGrip, 0F, 1F) // normalize front lateral forces

        lateralForcesRear = lowpass(lastRear, latForceRearY, 0.2F)
        lastRear = lateralForcesRear
        lateralForcesRear = clamp(abs(lateralForcesRear) * invMaxGrip, 0F, 1F) // normalize rear lateral forces

        // compute strength
        driftStrength = fixup((lateralForcesFront + lateralForcesRear) * 0.5F)

        if (hasCollided) {
            // ignore drifts for a couple of seconds
            if (collisionTime.elapsed().tickSeconds >= 0.5F) {
                collisionTime.stop()
                hasCollided = false
            }
        } else {
            if (!isDrifting) {
                // search for onBeginDrift
                if (driftStrength > 0.4F && velocityLength > 20) {
                    isDrifting = true
                    time.start()
                    GameEvents.driftState.trigger(car, PlayerDriftStateEvent.Type.OnBeginDrift)
                }
            } else {
                // search for onEndDrift
                if (driftStrength < 0.2F || velocityLength < 15F) {
                    time.stop()
                    isDrifting = false
                    GameEvents.driftState.trigger(car, PlayerDriftStateEvent.Type.OnEndDrift)
                }
            }
        }
    }
}
