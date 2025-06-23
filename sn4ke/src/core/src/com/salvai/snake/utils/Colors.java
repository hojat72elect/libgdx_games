package com.salvai.snake.utils;

import com.badlogic.gdx.graphics.Color;

public class Colors {


    final static Color[] SNAKE_BODY = {
            getColor(38, 222, 129), //green nephritis
            getColor(249, 168, 37), // yellow
            getColor(2, 119, 189),//blue
            getColor(216, 67, 21), //red
    };
    private final static Color[] SNAKE_HEAD = {
            getColor(32, 191, 107), //green emerald
            getColor(245, 127, 23),//yellow
            getColor(1, 87, 155),//blue
            getColor(191, 54, 12), //orange
    };


    public static Color getHeadColor(int index) {
        return SNAKE_HEAD[index];
    }

    public static Color getBodyColor(int index) {
        return SNAKE_BODY[index];
    }

    public static Color getBackgroundBlockColor(int index) {
        return SNAKE_BODY[(index) % SNAKE_BODY.length];
    }



    //converts value for libgdx color format
    private static Color getColor(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

    public static Color getBackgroundColor(int index) {
        return SNAKE_BODY[(index + 1) % SNAKE_BODY.length];
    }
}
