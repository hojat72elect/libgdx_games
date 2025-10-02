package com.bitfire.uracer.game.logic.types.helpers

import aurelienribon.tweenengine.Timeline
import aurelienribon.tweenengine.Tween
import aurelienribon.tweenengine.equations.Quad.Companion.OUT
import com.bitfire.uracer.game.tween.SysTweener.start
import com.bitfire.uracer.game.tween.SysTweener.stop
import com.bitfire.uracer.utils.AlgebraMath.clamp
import com.bitfire.uracer.utils.BoxedFloat
import com.bitfire.uracer.utils.BoxedFloatAccessor

class TimeModulator {

    private val timeMultiplier = BoxedFloat(1f)
    private var timeSeq = Timeline.createSequence()

    /**
     * returns the modulate time value.
     */
    val time = clamp(timeMultiplier.value, MIN_TIME, MAX_TIME)

    fun reset() {
        timeMultiplier.value = MAX_TIME
    }

    fun toDilatedTime() {
        modulateTo(MIN_TIME)
    }

    fun toNormalTime() {
        modulateTo(MAX_TIME)
    }

    private fun modulateTo(to: Float) {
        stop(timeMultiplier)
        timeSeq = Timeline.createSequence()
        timeSeq.push(Tween.to(timeMultiplier, BoxedFloatAccessor.VALUE, 1000f).target(to).ease(OUT))
        start(timeSeq)
    }

    companion object {
        const val MIN_TIME = 0.25F
        const val MAX_TIME = 1.0F
    }
}
