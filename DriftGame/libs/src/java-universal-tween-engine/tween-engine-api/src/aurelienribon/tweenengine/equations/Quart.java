package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

/**
 * Easing equation based on Robert Penner's work: <a href="http://robertpenner.com/easing/">Look here</a>
 */
public abstract class Quart extends TweenEquation {

    public static final Quart OUT = new Quart() {
        @Override
        public float compute(float t) {
            return -((t -= 1) * t * t * t - 1);
        }

        @Override
        public String toString() {
            return "Quart.OUT";
        }
    };

    public static final Quart INOUT = new Quart() {
        @Override
        public float compute(float t) {
            if ((t *= 2) < 1) return 0.5f * t * t * t * t;
            return -0.5f * ((t -= 2) * t * t * t - 2);
        }

        @Override
        public String toString() {
            return "Quart.INOUT";
        }
    };
}
