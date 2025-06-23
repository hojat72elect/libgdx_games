package com.salvai.snake.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

public class Constants {


    public static boolean DEV = false;

    public enum SCREEN {
        MENU,
        SETTINGS,
        GAME,
        SPLASH,
        LEVELCHOOSE
    }


    // Screen
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 1920;
    public static final int SCREEN_BLOCK_SIZE_SMALL = 60;
    public static final int SCREEN_BLOCK_SIZE_MEDIUM = 40;
    public static final int SCREEN_BLOCK_SIZE_BIG = 30;
    //World
    // ratio to screen is 1:20 with 1 block of border
    public static final int WORLD_BLOCK_SIZE = 1;
    public static final int PLAY_HEIGHT_FACTOR_X = 1;
    public static final int PLAY_HEIGHT_FACTOR_Y_SMALL = 4;
    public static final int PLAY_HEIGHT_FACTOR_Y_MEDIUM = 6;
    public static final int PLAY_HEIGHT_FACTOR_Y_BIG = 8;
    public static final int WORLD_TIME_MAX = 25; //25-5 = 20
    public static final int VIBRATION_DURATION = 80;
    public static final int VIBRATION_DURATION_GAME_OVER = 400;
    //BLOCK Index
    public static final int BLOCK_CELL = 1;
    public static final int SNAKE_HEAD_CELL = 2;
    //COLORS
    public static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 1);
    public static final int COLORS_SIZE = 4;


    //many numbers
    public static final int POINT = 1;
    public static final int WORLD_GAME_OVER_TIME = 10; //60 = 1 second. Time that passes before moving
    public static final int MAX_LEVEL = 60;
    public static final int LEVELS_PRO_TAB = 20;
    public static final float FADE_TIME = .2f;
    public static final int SWIPE_FACTOR = 16;
    //Filenames
    public static final String BLOCK_IMAGE_NAME = "images/block.png";
    public static final String APPLE_IMAGE_NAME = "images/apple.png";
    public static final String HAND_IMAGE_NAME = "images/hand.png";
    public static final String BACKGROUND_IMAGE = "images/background.png";
    public static final String SKIN_FILE_NAME = "skin/uiskin.json";
    public static final String SKIN_ATLAS_FILE_NAME = "skin/uiskin.atlas";
    public static final String LEVEL_PREVIEW = "level-preview/level-";
    public static final String POINT_SFX = "sound/point.m4a";
    public static final String UP_SFX = "sound/up.m4a";
    public static final String LEFT_RIGHT_SFX = "sound/left-right.m4a";
    public static final String DOWN_SFX = "sound/down.m4a";
    public static final String GAME_OVER_SFX = "sound/game-over.m4a";
    public static final String NEW_BEST_SFX = "sound/new-best.m4a";
    //background
    public static final int MAX_BACKGROUND_OBJECTS = 60;
    public static final int MIN_BACKGROUND_SPEED = 20;
    public static final int MAX_BACKGROUND_SPEED = 80;
    public static final int BACKGROUND_OBJECT_WIDTH = 6;
    public static final int BACKGROUND_OBJECT_HEIGHT = 6;
    //ANIMATIONS
    public static final float SCALE = 0.5f;
    public static final float SCORE_SCALE = 0.65f;
    public static final float SCORE_DURATION = 0.1f;
    public static final float HIGH_SCORE_DURATION = 0.5f;
    public static final float DURATION = 0.5f;
    public static final Interpolation INTERPOLATION = Interpolation.pow2;
    public static final Interpolation SNAKE_INTERPOLATION = Interpolation.smoother;
    public static final float SNAKE_DURATION = 0.1f;

    //DIALOGS
    public static int DIALOG_BUTTON_SIZE = 260;
    public static int DIALOG_BUTTON_PAD = 80;
    public static int DIALOG_BUTTON_SPACE = 120;
}
