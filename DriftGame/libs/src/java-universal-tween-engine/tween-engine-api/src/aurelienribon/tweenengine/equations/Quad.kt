package aurelienribon.tweenengine.equations

import aurelienribon.tweenengine.TweenEquation

/**
 * Easing equation based on Robert Penner's work: [Look here](http://robertpenner.com/easing/)
 */
abstract class Quad : TweenEquation() {
    companion object {
        val IN = object : Quad() {
            override fun compute(t: Float) = t * t
            override fun toString() = "Quad.IN"
        }

        val OUT = object : Quad() {
            override fun compute(t: Float) = -t * (t - 2)
            override fun toString() = "Quad.OUT"
        }

        val INOUT = object : Quad() {
            override fun compute(t: Float): Float {
                var t = t
                if ((2.let { t *= it; t }) < 1) return 0.5f * t * t
                return -0.5f * ((--t) * (t - 2) - 1)
            }

            override fun toString() = "Quad.INOUT"

        }
    }
}
