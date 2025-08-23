package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

/**
 * Easing equation based on Robert Penner's work: <a href="http://robertpenner.com/easing/">Look here</a>
 */
public abstract class Back extends TweenEquation {

    public static final Back INOUT = new Back() {
        @Override
        public float compute(float t) {
            float s = param_s;
            if ((t *= 2) < 1) return 0.5f * (t * t * (((s *= (1.525f)) + 1) * t - s));
            return 0.5f * ((t -= 2) * t * (((s *= (1.525f)) + 1) * t + s) + 2);
        }

        @Override
        public String toString() {
            return "Back.INOUT";
        }
    };

    protected float param_s = 1.70158f;

    public Back s(float s) {
        param_s = s;
        return this;
    }
}
