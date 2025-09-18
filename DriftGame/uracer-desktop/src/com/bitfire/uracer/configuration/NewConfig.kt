package com.bitfire.uracer.configuration

import aurelienribon.tweenengine.TweenEquation
import aurelienribon.tweenengine.equations.Quart
import com.bitfire.postprocessing.filters.Blur
import com.bitfire.uracer.game.logic.helpers.CameraController.InterpolationMode

object GraphicsUtils {
    const val ReferenceScreenWidth = 1280
    const val ReferenceScreenHeight = 720

    const val DefaultFadeMilliseconds = 500
    const val DefaultResetFadeMilliseconds = 500

    const val DefaultGhostCarOpacity = 0.25F
    const val DefaultTargetCarOpacity = 0.75F
    const val DefaultGhostOpacityChangeMs = 500F

    @JvmField
    val DefaultGhostOpacityChangeEq: TweenEquation = Quart.OUT
    const val EnableMipMapping = true

    @JvmField
    val CameraInterpolationMode = InterpolationMode.Linear
}

object PhysicsUtils {
    /**
     * defines how many pixels are 1 Box2d meter
     */
    const val PixelsPerMeter = 18.0F

    /**
     * defines physics dt duration, in hz
     */
    const val TimestepHz = 60.0F

    /**
     * defines the reference dt, in hz, to base damping and friction values on
     */
    const val PhysicsTimestepReferenceHz = 60.0F

    /**
     * defines time modifier
     */
    const val TimeMultiplier = 1F

    /**
     * defines physics dt duration, in seconds
     */
    @JvmField
    val Dt = 1.0F / TimestepHz

    /**
     * defines the minimum and maximum collision impact force to be considered
     */
    const val MaxImpactForce = 200F
    val OneOnMaxImpactForce = 1F / MaxImpactForce
}

object DebugUtils {
    const val UseDebugHelper = true
    const val TraverseWalls = false
    const val ApplyCarFrictionFromMap = true
    const val FrustumCulling = true
    const val InfiniteDilationTime = false
    const val PauseDisabled = true
}

object PostProcessingUtils {
    @JvmField
    val BlurType = Blur.BlurType.Gaussian5x5b
    const val BlurNumPasses = 2
    const val NormalDepthMapRatio = 1F
    const val FboRatio = 0.5F
}
