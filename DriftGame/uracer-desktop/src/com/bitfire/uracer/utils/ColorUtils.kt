package com.bitfire.uracer.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils

fun paletteRYG(amount: Float, alpha: Float): Color {
    val greenRatio = MathUtils.clamp(amount, 0.23F, 1F)
    val rbRange = (1 - MathUtils.clamp(greenRatio, 0.761F, 1F)) / (1 - 0.761F)

    val tmpcolor = Color()
    tmpcolor.r = 0.678F + (0.969F - 0.678F) * rbRange
    tmpcolor.g = greenRatio
    tmpcolor.b = 0.118F - (0.118F - 0.114F) * rbRange
    tmpcolor.a = alpha
    return tmpcolor
}

