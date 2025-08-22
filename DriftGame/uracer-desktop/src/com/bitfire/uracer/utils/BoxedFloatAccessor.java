package com.bitfire.uracer.utils;

import aurelienribon.tweenengine.TweenAccessor;

public final class BoxedFloatAccessor implements TweenAccessor<BoxedFloat> {
    public static final int VALUE = 1;

    @Override
    public int getValues(BoxedFloat target, int tweenType, float[] returnValues) {
        returnValues[0] = target.value;
        return 1;
    }

    @Override
    public void setValues(BoxedFloat target, int tweenType, float[] newValues) {
        target.value = newValues[0];
    }
}
