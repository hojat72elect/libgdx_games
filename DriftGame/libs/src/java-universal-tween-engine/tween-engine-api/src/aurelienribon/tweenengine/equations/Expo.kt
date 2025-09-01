package aurelienribon.tweenengine.equations

import aurelienribon.tweenengine.TweenEquation
import kotlin.math.pow

/**
 * Easing equation based on Robert Penner's work: [Look here](http://robertpenner.com/easing/)
 */
abstract class Expo : TweenEquation() {

    companion object {
        val INOUT = object : Expo() {
            override fun compute(t: Float): Float {
                var t = t
                if (t == 0f) return 0f
                if (t == 1f) return 1f
                if ((2.let { t *= it; t }) < 1) return 0.5f * 2.0.pow((10 * (t - 1)).toDouble()).toFloat()
                return 0.5f * (-2.0.pow((-10 * --t).toDouble()).toFloat() + 2)
            }

            override fun toString() = "Expo.INOUT"
        }
    }
}
