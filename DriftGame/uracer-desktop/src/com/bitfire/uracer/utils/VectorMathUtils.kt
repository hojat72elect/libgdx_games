package com.bitfire.uracer.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2

/**
 * Vector math utils.
 * Returns a vector in a top-left coordinate system so that:
 * up=[0,-1], left=[-1,0], right=[1,0], down=[0,1]
 */
object VectorMathUtils {

    private val retRad = Vector2()
    private val retDeg = Vector2()

    @JvmStatic
    fun fromRadians(radians: Float): Vector2 {
        retRad.set(AlgebraMath.fixup(-MathUtils.sin(radians)), AlgebraMath.fixup(-MathUtils.cos(radians)))
        return retRad
    }

    @JvmStatic
    fun fromDegrees(degrees: Float): Vector2 {
        val radians = degrees * MathUtils.degreesToRadians
        retDeg.set(fromRadians(radians))
        return retDeg
    }

    @JvmStatic
    fun toRadians(v: Vector2) = atan2(v.x, -v.y)

    @JvmStatic
    fun clamp(v: Vector2, min: Float, max: Float): Vector2 {
        v.x = AlgebraMath.clamp(v.x, min, max)
        v.y = AlgebraMath.clamp(v.y, min, max)
        return v
    }

    fun clamp(v: Vector2, xmin: Float, xmax: Float, ymin: Float, ymax: Float): Vector2 {
        v.x = AlgebraMath.clamp(v.x, xmin, xmax)
        v.y = AlgebraMath.clamp(v.y, ymin, ymax)
        return v
    }

    @JvmStatic
    fun fixup(v: Vector2): Vector2 {
        if ((v.x * v.x + v.y * v.y) < AlgebraMath.CMP_EPSILON) {
            v.x = 0F
            v.y = 0F
        }

        return v
    }

    @JvmStatic
    fun truncate(v: Vector2, maxLength: Float) {
        if (v.len() > maxLength) {
            v.nor().scl(maxLength)
        }
    }

    @JvmStatic
    fun truncateToInt(v: Vector2) {
        v.x = v.x.toInt().toFloat()
        v.y = v.y.toInt().toFloat()
    }
}
