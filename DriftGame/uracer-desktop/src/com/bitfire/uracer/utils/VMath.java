package com.bitfire.uracer.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Vector math utils.
 * Returns a vector in a top-left coordinate system so that:
 * up=[0,-1], left=[-1,0], right=[1,0], down=[0,1]
 */
public final class VMath {

    private static final Vector2 retRad = new Vector2();
    private static final Vector2 retDeg = new Vector2();

    private VMath() {
    }

    public static Vector2 fromRadians(float radians) {
        retRad.set(AMath.fixup(-MathUtils.sin(radians)), AMath.fixup(-MathUtils.cos(radians)));
        return retRad;
    }

    public static Vector2 fromDegrees(float degrees) {
        float radians = degrees * MathUtils.degreesToRadians;
        retDeg.set(VMath.fromRadians(radians));
        return retDeg;
    }

    public static float toRadians(Vector2 v) {
        return (float) Math.atan2(v.x, -v.y);
    }

    public static Vector2 clamp(Vector2 v, float min, float max) {
        v.x = AMath.clamp(v.x, min, max);
        v.y = AMath.clamp(v.y, min, max);
        return v;
    }

    public static Vector2 clamp(Vector2 v, float xmin, float xmax, float ymin, float ymax) {
        v.x = AMath.clamp(v.x, xmin, xmax);
        v.y = AMath.clamp(v.y, ymin, ymax);
        return v;
    }

    public static Vector2 fixup(Vector2 v) {
        if ((v.x * v.x + v.y * v.y) < AMath.CMP_EPSILON) {
            v.x = 0;
            v.y = 0;
        }

        return v;
    }

    public static void truncate(Vector2 v, float maxLength) {
        if (v.len() > maxLength) {
            v.nor().scl(maxLength);
        }
    }

    public static void truncateToInt(Vector2 v) {
        v.x = (int) v.x;
        v.y = (int) v.y;
    }
}
