package com.bitfire.uracer.utils

/**
 * Encapsulates a floating point value interpolated towards the specified target value by the specified alpha amount.
 */
class InterpolatedFloat @JvmOverloads constructor(private val reset: Float = 0F, @JvmField var fixup: Boolean = true) {

    private var previous = reset
    private var current = reset

    fun reset(resetState: Boolean) {
        reset(reset, resetState)
    }

    fun reset(value: Float, resetState: Boolean) {
        if (resetState) {
            this.previous = value
        }

        this.current = value
    }

    fun set(value: Float, alpha: Float): Float {
        current = AMath.lerp(previous, value, alpha)
        if (fixup) {
            current = AMath.fixup(current)
        }
        previous = current
        return current
    }

    fun get() = current

}
