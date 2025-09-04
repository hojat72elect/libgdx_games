package aurelienribon.tweenengine.equations

import aurelienribon.tweenengine.TweenEquation

/**
 * Easing equation based on Robert Penner's work: [Look here](http://robertpenner.com/easing/)
 */
abstract class Quart : TweenEquation() {
    companion object {
        val OUT = object : Quart() {
            override fun compute(t: Float): Float {
                var t = t
                return -((1.let { t -= it; t }) * t * t * t - 1)
            }

            override fun toString() = "Quart.OUT"

        }
    }
}
