package com.salvai.centrum.utils;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class ColissionUtil {


    public static boolean circleHitsCircle(Circle circle, Circle circle2) {
        return Intersector.overlaps(circle, circle2);
    }

}
