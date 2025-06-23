package com.salvai.centrum.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class RandomUtil {
    public static Random random = new Random();

    public static Vector2 getRandomEnemyCoordinates() {
        if (random.nextBoolean())
            return new Vector2(random.nextBoolean() ? 0 : Constants.SCREEN_WIDTH, random.nextInt(Constants.SCREEN_HEIGHT));
        else
            return new Vector2(random.nextInt(Constants.SCREEN_WIDTH), random.nextBoolean() ? 0 : Constants.SCREEN_HEIGHT);
    }


    public static int getRandomEnemySpeed(int max) {
        return random.nextInt(max - Constants.ENEMY_MIN_SPEED > 0 ? max - Constants.ENEMY_MIN_SPEED : 1) + Constants.ENEMY_MIN_SPEED;
    }

    public static int getRandomDelay() {
        return random.nextInt(Constants.ENEMY_MAX_DELAY) + Constants.ENEMY_MIN_DELAY;
    }

    public static Color getRandomColor() {
        return GameColorPalette.BASIC[random.nextInt(GameColorPalette.BASIC.length)];
    }

    public static Color getRandomColorNoWhite() {
        return GameColorPalette.BASIC[random.nextInt(GameColorPalette.BASIC.length-1) + 1];
    }

    //BACKGROUND
    public static int getRandomBackgroundStarYCoordinate() {
        return random.nextInt(Constants.SCREEN_HEIGHT);
    }

    public static int getRandomBackgroundStarXCoordinate() {
        return random.nextInt(Constants.SCREEN_WIDTH);
    }

    public static float getRandomAlpha() {
        return random.nextFloat();
    }

    public static int getRandomStarSpeed() {
        return random.nextInt(Constants.MAX_STAR_SPEED - Constants.MIN_STAR_SPEED) + Constants.MIN_STAR_SPEED;
    }
}
