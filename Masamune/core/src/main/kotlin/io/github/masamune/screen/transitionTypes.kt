package io.github.masamune.screen

import com.badlogic.gdx.math.Interpolation

sealed interface TransitionType

data object DefaultTransitionType : TransitionType

data class BlurTransitionType(
    val startBlur: Float,
    val endBlur: Float,
    val time: Float,
    val startAlpha: Float = 1f,
    val endAlpha: Float = 1f,
    val interpolation: Interpolation = Interpolation.linear,
) : TransitionType

data class FadeTransitionType(
    val startAlpha: Float,
    val endAlpha: Float,
    val time: Float,
    val interpolation: Interpolation = Interpolation.linear,
    val delayInSeconds: Float = 0f,
) : TransitionType
