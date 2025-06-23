package com.salvai.centrum.utils;


import com.badlogic.gdx.graphics.Color;

public class Constants {
    // Game
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;
    public static final int WIDTH_CENTER = 240;
    public static final int HEIGHT_CENTER = 400;
    public static final int MIN_DIFFICULTY = 3;
    public static final int MAX_DIFFICULTY = 6; //set the maximal amount of enemies
    public static final int DIFFICULTY_CHANGE = 15;


    //COLORS
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 1);

    //Ball
    public static final float PLANET_DIAMETER = 80;

    //Enemies
    public static final int ENEMY_MIN_SPEED = 120;
    public static final int ENEMY_MAX_SPEED = 190; //
    public static final int ENEMY_TOTAL_MAX_SPEED = 300;
    public static final int ENEMY_DIAMETER = 30;
    public static final float ENEMY_SPEED_INCREASE = 0.1f;
    public static final int ENEMY_MIN_DELAY = 8;
    public static final int ENEMY_MAX_DELAY = 42;

    //Missiles
    public static final float MISSILE_SPEED = 490;
    public static final float MISSILE_RADIUS = 10;


    //images
    public static final String BALL_IMAGE_NAME = "images/ball.png";
    public static final String PAUSE_BUTTON_IMAGE_NAME = "images/pause.png";
    public static final String SPLASH_IMAGE_NAME = "images/splash.png";
    public static final String STAR_IMAGE_NAME = "images/star.png";
    public static final String LEVEL_STARS_IMAGE_NAME = "images/level-stars.png";

    //sounds
    public static final String POP_SOUND_NAME = "sounds/pop.wav";
    public static final String COIN_SOUND_NAME = "sounds/coin.wav";
    public static final String COMBO_RESET_SOUND_NAME = "sounds/combo-reset.wav";
    public static final String GAME_OVER_SOUND_NAME = "sounds/game-over.wav";
    public static final String SUCCESS_SOUND_NAME = "sounds/success.wav";

    //others
    public static final String FONT_NAME = "jellee.roman.ttf";
    public static final String WHITE_FONT_NAME = "white.ttf";
    public static final String PARTICLE_EFFECT_FILE_NAME = "ball-explosion";
    public static final String SKIN_FILE_NAME = "skin/uiskin.json";
    public static final String SKIN_ATLAS_FILE_NAME = "skin/uiskin.atlas";

    //background
    public static final int MAX_STARS = 30;
    public static final int MIN_STAR_SPEED = 1;
    public static final int MAX_STAR_SPEED = 3;
    public static final int STAR_SIZE = 4;

    public static final int MAX_LEVEL = 100;

    public static final float FADE_TIME = .1f;
}