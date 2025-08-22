package aurelienribon.tweenengine;

/**
 * Base class for every easing equation. You can create your own equations and directly use them in the Tween engine by inheriting
 * from this class.
 *
 * @see Tween
 */
public abstract class TweenEquation {

    /**
     * Computes the next value of the interpolation.
     *
     * @param t The current time, between 0 and 1.
     * @return The current value.
     */
    public abstract float compute(float t);

}
