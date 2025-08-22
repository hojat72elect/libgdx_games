package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

/**
 * Easing equation based on Robert Penner's work: http://robertpenner.com/easing/
 */
public abstract class Quad extends TweenEquation {
    public static final Quad IN = new Quad() {
        @Override
        public float compute(float t) {
            return t * t;
        }

        @Override
        public String toString() {
            return "Quad.IN";
        }
    };

    public static final Quad OUT = new Quad() {
        @Override
        public float compute(float t) {
            return -t * (t - 2);
        }

        @Override
        public String toString() {
            return "Quad.OUT";
        }
    };

    public static final Quad INOUT = new Quad() {
        @Override
        public float compute(float t) {
            if ((t *= 2) < 1) return 0.5f * t * t;
            return -0.5f * ((--t) * (t - 2) - 1);
        }

        @Override
        public String toString() {
            return "Quad.INOUT";
        }
    };
}
