package com.bitfire.uracer.game

import com.bitfire.uracer.configuration.PhysicsUtils
import com.bitfire.uracer.utils.AlgebraMath.damping

object GameplaySettings {
    @JvmField
    val DampingFriction = damping(0.975f)

    // maximum amount of seconds for the wrong way detector before the lap is completely invalidated
    const val MAX_SECONDS_WRONG_WAY_DETECTOR = 1.0F

    // a replay is discarded if its length is less than the specified seconds
    const val REPLAY_MIN_DURATION_SECONDS = 4F

    const val REPLAY_MIN_DURATION_TICKS = (REPLAY_MIN_DURATION_SECONDS * PhysicsUtils.TimestepHz).toInt()
    const val COLLISION_FACTOR_MIN_DURATION_MS = 500F
    const val COLLISION_FACTOR_MAX_DURATION_MS = 2_500F

    enum class TimeDilateInputMode {
        Toggle,  // touch to activate, touch again to deactivate
        TouchAndRelease,  // touch to activate, release to deactivate
    }
}
