package aurelienribon.tweenengine.equations

import aurelienribon.tweenengine.TweenEquation

/**
 * Easing equation based on [Robert Penner's work](http://robertpenner.com/easing/)
 */
abstract class Linear() : TweenEquation() {
    companion object {
        val INOUT = object : Linear() {
            override fun compute(t: Float) = t
        }
    }

    override fun toString() = "Linear.INOUT"
}