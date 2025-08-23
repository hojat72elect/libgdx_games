package com.bitfire.uracer.game.logic.types.helpers;

import com.bitfire.uracer.game.tween.SysTweener;
import com.bitfire.uracer.utils.AMath;
import com.bitfire.uracer.utils.BoxedFloat;
import com.bitfire.uracer.utils.BoxedFloatAccessor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;

public final class TimeModulator {

    public static final float MinTime = 0.25f;
    public static final float MaxTime = 1.0f;

    private final BoxedFloat timeMultiplier;
    private Timeline timeSeq;

    public TimeModulator() {
        timeMultiplier = new BoxedFloat(1);
        timeSeq = Timeline.createSequence();
    }

    // returns the modulate time value
    public float getTime() {
        return AMath.clamp(timeMultiplier.value, MinTime, MaxTime);
    }

    public void reset() {
        timeMultiplier.value = MaxTime;
    }

    public void toDilatedTime() {
        modulateTo(MinTime);
    }

    public void toNormalTime() {
        modulateTo(MaxTime);
    }

    private void modulateTo(float to) {
        SysTweener.stop(timeMultiplier);
        timeSeq = Timeline.createSequence();
        timeSeq.push(Tween.to(timeMultiplier, BoxedFloatAccessor.VALUE, (float) 1000).target(to).ease(Quad.OUT));
        SysTweener.start(timeSeq);
    }
}
