package aurelienribon.tweenengine.paths

import aurelienribon.tweenengine.TweenPath
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class CatmullRom : TweenPath {
    override fun compute(t: Float, points: FloatArray, pointsCnt: Int): Float {
        var t = t
        var segment = floor((pointsCnt - 1) * t).toInt()
        segment = max(segment, 0)
        segment = min(segment, pointsCnt - 2)
        t = t * (pointsCnt - 1) - segment

        if (segment == 0) return catmullRomSpline(points[0], points[0], points[1], points[2], t)

        if (segment == pointsCnt - 2) return catmullRomSpline(points[pointsCnt - 3], points[pointsCnt - 2], points[pointsCnt - 1], points[pointsCnt - 1], t)

        return catmullRomSpline(points[segment - 1], points[segment], points[segment + 1], points[segment + 2], t)
    }

    private fun catmullRomSpline(a: Float, b: Float, c: Float, d: Float, t: Float): Float {
        val t1 = (c - a) * 0.5f
        val t2 = (d - b) * 0.5f
        val h1 = 2 * t * t * t - 3 * t * t + 1
        val h2 = -2 * t * t * t + 3 * t * t
        val h3 = t * t * t - 2 * t * t + t
        val h4 = t * t * t - t * t

        return b * h1 + c * h2 + t1 * h3 + t2 * h4
    }
}
