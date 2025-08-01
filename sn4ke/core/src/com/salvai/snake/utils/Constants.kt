package com.salvai.snake.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation

object Constants {
    // Screen
    const val SCREEN_WIDTH: Int = 1080
    const val SCREEN_HEIGHT: Int = 1920
    const val SCREEN_BLOCK_SIZE_SMALL: Int = 60
    const val SCREEN_BLOCK_SIZE_MEDIUM: Int = 40
    const val SCREEN_BLOCK_SIZE_BIG: Int = 30

    //World
    // ratio to screen is 1:20 with 1 block of border
    const val WORLD_BLOCK_SIZE: Int = 1
    const val PLAY_HEIGHT_FACTOR_X: Int = 1
    const val PLAY_HEIGHT_FACTOR_Y_SMALL: Int = 4
    const val PLAY_HEIGHT_FACTOR_Y_MEDIUM: Int = 6
    const val PLAY_HEIGHT_FACTOR_Y_BIG: Int = 8
    const val WORLD_TIME_MAX: Int = 25 //25-5 = 20
    const val VIBRATION_DURATION: Int = 80
    const val VIBRATION_DURATION_GAME_OVER: Int = 400

    //BLOCK Index
    const val BLOCK_CELL: Int = 1
    const val SNAKE_HEAD_CELL: Int = 2

    //COLORS
    @JvmField
    val BACKGROUND_COLOR: Color = Color(255f, 255f, 255f, 1f)
    const val COLORS_SIZE: Int = 4

    //many numbers
    const val POINT: Int = 1
    const val WORLD_GAME_OVER_TIME: Int = 10 //60 = 1 second. Time that passes before moving
    const val MAX_LEVEL: Int = 60
    const val LEVELS_PRO_TAB: Int = 20
    const val FADE_TIME: Float = .2f
    const val SWIPE_FACTOR: Int = 16

    //Filenames
    const val BLOCK_IMAGE_NAME: String = "images/block.png"
    const val APPLE_IMAGE_NAME: String = "images/apple.png"
    const val HAND_IMAGE_NAME: String = "images/hand.png"
    const val BACKGROUND_IMAGE: String = "images/background.png"
    const val SKIN_FILE_NAME: String = "skin/uiskin.json"
    const val SKIN_ATLAS_FILE_NAME: String = "skin/uiskin.atlas"
    const val LEVEL_PREVIEW: String = "level-preview/level-"

    //background
    const val MAX_BACKGROUND_OBJECTS: Int = 60
    const val MIN_BACKGROUND_SPEED: Int = 20

    const val MAX_BACKGROUND_SPEED: Int = 80
    const val BACKGROUND_OBJECT_WIDTH: Int = 6
    const val BACKGROUND_OBJECT_HEIGHT: Int = 6

    //ANIMATIONS
    const val SCALE: Float = 0.5f
    const val SCORE_SCALE: Float = 0.65f
    const val SCORE_DURATION: Float = 0.1f
    const val HIGH_SCORE_DURATION: Float = 0.5f
    const val DURATION: Float = 0.5f

    @JvmField
    val INTERPOLATION: Interpolation = Interpolation.pow2

    @JvmField
    val SNAKE_INTERPOLATION: Interpolation = Interpolation.smoother
    const val SNAKE_DURATION: Float = 0.1f

    //DIALOGS
    @JvmField
    var DIALOG_BUTTON_SIZE: Int = 260

    @JvmField
    var DIALOG_BUTTON_PAD: Int = 80

    @JvmField
    var DIALOG_BUTTON_SPACE: Int = 120

    enum class SCREEN {
        MENU,
        SETTINGS,
        LEVELCHOOSE
    }
}
