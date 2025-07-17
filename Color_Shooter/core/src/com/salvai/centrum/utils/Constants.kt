package com.salvai.centrum.utils

import com.badlogic.gdx.graphics.Color

object Constants {
    // Game
    const val SCREEN_WIDTH = 480
    const val SCREEN_HEIGHT = 800
    const val WIDTH_CENTER = 240
    const val HEIGHT_CENTER = 400
    const val MIN_DIFFICULTY = 3
    const val MAX_DIFFICULTY = 6 //set the maximal amount of enemies
    const val DIFFICULTY_CHANGE = 15


    //COLORS
    @JvmField
    val BACKGROUND_COLOR = Color(0f, 0f, 0f, 1f)

    //Ball
    const val PLANET_DIAMETER = 80f

    //Enemies
    const val ENEMY_MIN_SPEED = 120
    const val ENEMY_MAX_SPEED = 190 //
    const val ENEMY_TOTAL_MAX_SPEED = 300
    const val ENEMY_DIAMETER = 30
    const val ENEMY_SPEED_INCREASE = 0.1f
    const val ENEMY_MIN_DELAY = 8
    const val ENEMY_MAX_DELAY = 42

    //Missiles
    const val MISSILE_SPEED = 490f
    const val MISSILE_RADIUS = 10f

    //images
    const val BALL_IMAGE_NAME = "images/ball.png"
    const val PAUSE_BUTTON_IMAGE_NAME = "images/pause.png"
    const val SPLASH_IMAGE_NAME = "images/splash.png"
    const val STAR_IMAGE_NAME = "images/star.png"
    const val LEVEL_STARS_IMAGE_NAME = "images/level-stars.png"

    //sounds
    const val POP_SOUND_NAME = "sounds/pop.wav"
    const val COIN_SOUND_NAME = "sounds/coin.wav"
    const val COMBO_RESET_SOUND_NAME = "sounds/combo-reset.wav"
    const val GAME_OVER_SOUND_NAME = "sounds/game-over.wav"
    const val SUCCESS_SOUND_NAME = "sounds/success.wav"

    // Particle effects
    const val PARTICLE_EFFECT_FILE_NAME = "ball-explosion"
    const val SKIN_FILE_NAME = "skin/uiskin.json"

    // UI skin
    const val SKIN_ATLAS_FILE_NAME = "skin/uiskin.atlas"

    //background
    const val MAX_STARS = 30
    const val MIN_STAR_SPEED = 1
    const val MAX_STAR_SPEED = 3
    const val STAR_SIZE = 4

    const val MAX_LEVEL = 100

    const val FADE_TIME = .1f
}