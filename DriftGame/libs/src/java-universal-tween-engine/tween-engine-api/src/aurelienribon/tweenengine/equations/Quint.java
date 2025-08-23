package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

/**
 * Easing equation based on Robert Penner's work: <a href="http://robertpenner.com/easing/">Look here</a>
 */
public abstract class Quint extends TweenEquation {

    public static final Quint OUT = new Quint() {
        @Override
        public float compute(float t) {
            return (t -= 1) * t * t * t * t + 1;
        }

        @Override
        public String toString() {
            return "Quint.OUT";
        }
    };

    public static final Quint INOUT = new Quint() {
        @Override
        public float compute(float t) {
            if ((t *= 2) < 1) return 0.5f * t * t * t * t * t;
            return 0.5f * ((t -= 2) * t * t * t * t + 2);
        }

        @Override
        public String toString() {
            return "Quint.INOUT";
        }
    };
}
