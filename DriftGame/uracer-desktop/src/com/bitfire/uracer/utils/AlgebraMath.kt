package com.bitfire.uracer.utils

import com.bitfire.uracer.configuration.Config
import kotlin.math.*

/**
 * This is a collection of math utilities for performing algebraic operations.
 */
object AlgebraMath {
    const val TWO_PI = 6.28318530717958647692F
    const val PI = 3.14159265358979323846F
    const val PI_4 = 0.785398163397448309616F

    const val CMP_EPSILON = 0.001F
    const val ONE_ON_CMP_EPSILON = 1000F
    private const val SigmoidTAmplitude = 5F // good values in the range [3, 8]
    private const val SigmoidTStepSize = 3F

    @JvmStatic
    fun equals(a: Float, b: Float) = abs(abs(a) - abs(b)) < CMP_EPSILON

    @JvmStatic
    fun isZero(a: Float) = abs(a) < CMP_EPSILON

    @JvmStatic
    fun lerp(prev: Float, curr: Float, alpha: Float) = curr * alpha + prev * (1F - alpha)

    @JvmStatic
    fun lowpass(prev: Float, curr: Float, alpha: Float) = lerp(prev, curr, alpha)

    @JvmStatic
    fun clamp(value: Float, min: Float, max: Float) = min(max, max(min, value))

    @JvmStatic
    fun clamp(value: Long, min: Long, max: Long) = min(max, max(min, value))

    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int) = min(max, max(min, value))

    @JvmStatic
    fun fixup(v: Float) = if (abs(v) < CMP_EPSILON) 0F else v

    fun fixup(v: Float, epsilon: Float) = if (abs(v) < epsilon) 0F else v

    @JvmStatic
    fun fixupTo(v: Float, target: Float) = if (abs(abs(v) - abs(target)) < CMP_EPSILON) target else v

    @JvmStatic
    fun clampf(v: Float, min: Float, max: Float) = clamp(fixupTo(fixupTo(v, min), max), min, max)

    @JvmStatic
    fun sign(v: Float) = if (v < 0) -1F else 1F

    @JvmStatic
    fun normalRelativeAngle(angleRad: Float): Float {
        val wrapped = angleRad % TWO_PI
        return when {
            wrapped >= PI -> wrapped - TWO_PI
            wrapped < -PI -> wrapped + TWO_PI
            else -> wrapped
        }
    }

    @JvmStatic
    fun sigmoid(strength: Float) = (1F / (1F + Math.E.pow(-strength.toDouble()))).toFloat()

    /**
     * Tweakable normalized sigmoid function based on Dino Dini's implementation (see "sigmoid" IPython notebook)
     *
     * Formulae: sign(x) * ( abs(x)*k / (1+k-abs(x)) )
     *
     * @param x  an input value in the range [-1,1]
     * @param k  an amplitude modulation epsilon in the range [0,1]
     * @param up Whether the amplitude refers to the width or the height
     * @return the modulated input value x
     */
    @JvmStatic
    fun sigmoidT(x: Float, k: Float, up: Boolean): Float {
        val sgn = if (x >= 0) 1F else -1F
        val absx = abs(x)
        val absk = abs(k)
        val n = absk * SigmoidTAmplitude
        var _k = (1.0F / SigmoidTStepSize.toDouble().pow(n.toDouble()).toFloat()) * SigmoidTAmplitude
        if (up) _k = -_k - 1
        return sgn * ((absx * _k) / (1 + _k - absx))
    }

    fun round(value: Float, decimal: Int): Float {
        val p = (10.0).pow(decimal.toDouble()).toFloat()
        val tmp = (value * p).roundToInt()
        return tmp / p
    }

    @JvmStatic
    fun normalizeImpactForce(force: Float): Float {
        var v = clamp(force, 0F, Config.Physics.MaxImpactForce)
        v *= Config.Physics.OneOnMaxImpactForce
        return v
    }

    /**
     * Compute a timestep-dependent damping factor from the specified time-independent constant and arbitrary factor.
     *
     * This isn't the only way to compute it:
     *
     * let 0.975 be the time-DEPENDENT factor
     * factor ^ (factor_good_at_timestep * dt)
     * or
     * pow( factor, factor_good_at_timestep * dt )
     *
     * that is:
     * pow( 0.975, 60 * dt )
     * thus, for a 60hz timestep:
     * pow( 0.975, 60 * (1/60) ) = 0.975		|	exp( -1.5 * (1/60) ) = 0.975309 (w/ 1.5 found by trial and error)
     * and for a 30hz timestep:
     * pow( 0.975, 60 * (1/30) ) = 0.950625		|	exp( -1.5 * (1/30) ) = 0.951229
     *
     * (see my post [here](http://www.gamedev.net/topic/624172-framerate-independent-friction/page__st__20))
     */
    @JvmStatic
    fun damping(factor: Float) = factor.toDouble().pow((Config.Physics.PhysicsTimestepReferenceHz * Config.Physics.Dt).toDouble()).toFloat()

}
