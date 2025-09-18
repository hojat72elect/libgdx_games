package com.bitfire.uracer.game;

import com.bitfire.uracer.configuration.PhysicsUtils;
import com.bitfire.uracer.utils.AlgebraMath;

public final class GameplaySettings {
    public static final float DampingFriction = AlgebraMath.damping(0.975f);
    // maximum amount of seconds for the wrong way detector before the lap is completely invalidated
    public static final float MaxSecondsWrongWayDetector = 1.0f;
    // a replay is discarded if its length is less than the specified seconds
    public static final float ReplayMinDurationSecs = 4f;
    public static final int ReplayMinDurationTicks = (int) (ReplayMinDurationSecs * PhysicsUtils.TimestepHz);
    public static final float CollisionFactorMinDurationMs = 500;
    public static final float CollisionFactorMaxDurationMs = 2500;

    private GameplaySettings() {
    }

    public enum TimeDilateInputMode {
        Toggle,                // touch to activate, touch again to deactivate
        TouchAndRelease,    // touch to activate, release to deactivate
    }
}
