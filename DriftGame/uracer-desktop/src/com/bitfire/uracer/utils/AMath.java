package com.bitfire.uracer.utils;

import com.bitfire.uracer.configuration.Config;

/**
 * Algebra math utils.
 */
public final class AMath {
    public static final float TWO_PI = 6.28318530717958647692f;
    public static final float PI = 3.14159265358979323846f;
    public static final float PI_4 = 0.785398163397448309616f;

    public static final float CMP_EPSILON = 0.001f;
    public static final float ONE_ON_CMP_EPSILON = 1000f;
    private static final float SigmoidTAmplitude = 5; // good values in the range [3, 8]
    private static final float SigmoidTStepSize = 3;

    private AMath() {
    }

    public static boolean equals(float a, float b) {
        return Math.abs(Math.abs(a) - Math.abs(b)) < CMP_EPSILON;
    }

    public static boolean isZero(float a) {
        return Math.abs(a) < CMP_EPSILON;
    }

    public static float lerp(float prev, float curr, float alpha) {
        return curr * alpha + prev * (1f - alpha);
    }

    public static float lowpass(float prev, float curr, float alpha) {
        return lerp(prev, curr, alpha);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(max, Math.max(min, value));
    }

    public static long clamp(long value, long min, long max) {
        return Math.min(max, Math.max(min, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    public static float fixup(float v) {
        if (Math.abs(v) < CMP_EPSILON) {
            return 0;
        }

        return v;
    }

    public static float fixup(float v, float epsilon) {
        if (Math.abs(v) < epsilon) {
            return 0;
        }

        return v;
    }

    public static float fixupTo(float v, float target) {
        if (Math.abs(Math.abs(v) - Math.abs(target)) < CMP_EPSILON) {
            return target;
        }

        return v;
    }

    public static float clampf(float v, float min, float max) {
        return AMath.clamp(AMath.fixupTo(AMath.fixupTo(v, min), max), min, max);
    }


    public static float sign(float v) {
        if (v < 0) {
            return -1f;
        }

        return 1f;
    }

    public static float normalRelativeAngle(float angleRad) {
        float wrapped = (angleRad % TWO_PI);
        return wrapped >= 0 ? (wrapped < PI) ? wrapped : wrapped - TWO_PI : (wrapped >= -PI) ? wrapped : wrapped + TWO_PI;
    }

    public static float sigmoid(float strength) {
        return (float) (1f / (1f + Math.pow(Math.E, -strength)));
    }

    /**
     * Tweakable normalized sigmoid function based on Dino Dini's implementation (see "sigmoid" IPython notebook)
     * <p>
     * Formulae: sign(x) * ( abs(x)*k / (1+k-abs(x)) )
     *
     * @param x  an input value in the range [-1,1]
     * @param k  an amplitude modulation epsilon in the range [0,1]
     * @param up Whether the amplitude refers to the width or the height
     * @return the modulated input value x
     */
    public static float sigmoidT(float x, float k, boolean up) {
        float sgn = x >= 0 ? 1 : -1;
        float absx = Math.abs(x);
        float absk = Math.abs(k);
        float n = absk * SigmoidTAmplitude;
        float _k = (1.0f / (float) Math.pow(SigmoidTStepSize, n)) * SigmoidTAmplitude;
        if (up) _k = -_k - 1;
        return sgn * ((absx * _k) / (1 + _k - absx));
    }

    public static float round(float value, int decimal) {
        float p = (float) Math.pow(10, decimal);
        float tmp = Math.round(value * p);
        return tmp / p;
    }

    public static float normalizeImpactForce(float force) {
        float v = AMath.clamp(force, 0, Config.Physics.MaxImpactForce);
        v *= Config.Physics.OneOnMaxImpactForce;
        return v;
    }

    /**
     * Compute a timestep-dependent damping factor from the specified time-independent constant and arbitrary factor.
     * <p>
     * This isn't the only way to compute it:
     * <p>
     * let 0.975 be the time-DEPENDENT factor
     * factor ^ (factor_good_at_timestep * dt)
     * or
     * pow( factor, factor_good_at_timestep * dt )
     * <p>
     * that is:
     * pow( 0.975, 60 * dt )
     * thus, for a 60hz timestep:
     * pow( 0.975, 60 * (1/60) ) = 0.975			|	exp( -1.5 * (1/60) ) = 0.975309 (w/ 1.5 found by trial and error)
     * and for a 30hz timestep:
     * pow( 0.975, 60 * (1/30) ) = 0.950625		|	exp( -1.5 * (1/30) ) = 0.951229
     * <p>
     * (see my post <a href="http://www.gamedev.net/topic/624172-framerate-independent-friction/page__st__20">here</a>)
     */
    public static float damping(float factor) {
        return (float) Math.pow(factor, Config.Physics.PhysicsTimestepReferenceHz * Config.Physics.Dt);
    }
}
