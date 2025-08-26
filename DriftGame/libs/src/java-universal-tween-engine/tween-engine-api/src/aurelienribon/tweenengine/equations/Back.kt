package aurelienribon.tweenengine.equations

import aurelienribon.tweenengine.TweenEquation

/**
 * Easing equation based on Robert Penner's work: [Look here](http://robertpenner.com/easing/).
 */
abstract class Back : TweenEquation() {
    protected var param_s = 1.70158F

    fun s(s: Float): Back {
        param_s = s
        return this
    }

    companion object {
        val INOUT = object : Back() {
            override fun compute(t: Float): Float {
                var t = t
                var s = param_s
                if ((2.let { t *= it; t }) < 1) {
                    return 0.5f * (t * t * (((1.525f.let { s *= it; s }) + 1) * t - s))
                }
                return 0.5f * ((2.let { t -= it; t }) * t * (((1.525f.let { s *= it; s }) + 1) * t + s) + 2)
            }

            override fun toString() = "Back.INOUT"

        }
    }
}
