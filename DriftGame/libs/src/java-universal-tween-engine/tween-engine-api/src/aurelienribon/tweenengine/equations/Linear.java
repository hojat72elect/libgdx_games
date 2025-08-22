package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

/**
 * Easing equation based on Robert Penner's work: <a href="http://robertpenner.com/easing/">Look here</a>
 */
public abstract class Linear extends TweenEquation {
    public static final Linear INOUT = new Linear() {
        @Override
        public float compute(float t) {
            return t;
        }

        @Override
        public String toString() {
            return "Linear.INOUT";
        }
    };
}
