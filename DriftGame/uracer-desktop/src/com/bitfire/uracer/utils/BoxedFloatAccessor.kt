package com.bitfire.uracer.utils

import aurelienribon.tweenengine.TweenAccessor

class BoxedFloatAccessor : TweenAccessor<BoxedFloat> {

    override fun getValues(target: BoxedFloat, tweenType: Int, returnValues: FloatArray): Int {
        returnValues[0] = target.value
        return 1
    }

    override fun setValues(target: BoxedFloat, tweenType: Int, newValues: FloatArray) {
        target.value = newValues[0]
    }

    companion object {
        const val VALUE = 1
    }
}
