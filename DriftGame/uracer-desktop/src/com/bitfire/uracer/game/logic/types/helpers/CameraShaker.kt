package com.bitfire.uracer.game.logic.types.helpers

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.bitfire.uracer.utils.InterpolatedFloat
import com.bitfire.uracer.utils.ScaleUtils

private const val PIXELS = 256F

class CameraShaker {

    var result = Vector2()
    var noiseX = InterpolatedFloat()
    var noiseY = InterpolatedFloat()

    fun compute(factor: Float): Vector2 {
        var px = PIXELS
        px *= ScaleUtils.Scale

        val radiusX = MathUtils.random(-px, px)
        val radiusY = MathUtils.random(-px, px)
        val noiseAlpha = 0.1F
        noiseX.set(radiusX, noiseAlpha)
        noiseY.set(radiusY, noiseAlpha)
        result.set(noiseX.get() * factor, noiseY.get() * factor)
        return result
    }
}
