package com.salvai.snake.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class RandomUtil {
    public static Random random = new Random();

    //BACKGROUND
    public static int getRandomBackgroundStarYCoordinate(int height) {
        return random.nextInt(height);
    }

    public static int getRandomCloudSizeFactor() {
        return random.nextInt(3) + 1;
    }

    public static int getRandomBackgroundStarXCoordinate(int width) {
        return random.nextInt(width);
    }

    public static float getRandomAlpha() {
        return random.nextFloat() * 0.5f;
    }

    public static Color getRandomColor() {
        return new Color(Colors.SNAKE_BODY[random.nextInt(Colors.SNAKE_BODY.length)]);
    }

    public static int getRandomStarSpeed() {
        return random.nextInt(Constants.MAX_BACKGROUND_SPEED - Constants.MIN_BACKGROUND_SPEED) + Constants.MIN_BACKGROUND_SPEED;
    }
}
