package aurelienribon.tweenengine.paths

import aurelienribon.tweenengine.TweenPath
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class Linear : TweenPath {
    override fun compute(t: Float, points: FloatArray, pointsCnt: Int): Float {
        var t = t
        var segment = floor(((pointsCnt - 1) * t)).toInt()
        segment = max(segment, 0)
        segment = min(segment, pointsCnt - 2)

        t = t * (pointsCnt - 1) - segment

        return points[segment] + t * (points[segment + 1] - points[segment])
    }
}
