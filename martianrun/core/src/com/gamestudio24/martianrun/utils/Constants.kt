package com.gamestudio24.martianrun.utils

import com.badlogic.gdx.math.Vector2

object Constants {
    const val GAME_NAME: String = "Martian Run!"

    const val APP_WIDTH: Int = 800
    const val APP_HEIGHT: Int = 480
    const val WORLD_TO_SCREEN: Float = 32f

    val WORLD_GRAVITY: Vector2 = Vector2(0f, -10f)

    const val GROUND_X: Float = 0f
    const val GROUND_Y: Float = 0f
    const val GROUND_WIDTH: Float = 25f
    const val GROUND_HEIGHT: Float = 2f
    const val GROUND_DENSITY: Float = 0f

    const val RUNNER_X: Float = 2f

    const val RUNNER_Y: Float = GROUND_Y + GROUND_HEIGHT
    const val RUNNER_WIDTH: Float = 1f
    const val RUNNER_HEIGHT: Float = 2f
    const val RUNNER_GRAVITY_SCALE: Float = 3f
    const val RUNNER_DENSITY: Float = 0.5f
    const val RUNNER_DODGE_X: Float = 2f
    const val RUNNER_DODGE_Y: Float = 1.5f

    @JvmField
    val RUNNER_JUMPING_LINEAR_IMPULSE: Vector2 = Vector2(0f, 13f)
    const val RUNNER_HIT_ANGULAR_IMPULSE: Float = 10f

    const val ENEMY_X: Float = 25f
    const val ENEMY_DENSITY: Float = RUNNER_DENSITY
    const val RUNNING_SHORT_ENEMY_Y: Float = 1.5f
    const val RUNNING_LONG_ENEMY_Y: Float = 2f
    const val FLYING_ENEMY_Y: Float = 3f

    @JvmField
    val ENEMY_LINEAR_VELOCITY: Vector2 = Vector2(-10f, 0f)

    const val BACKGROUND_ASSETS_ID: String = "background"
    const val GROUND_ASSETS_ID: String = "ground"
    const val RUNNER_RUNNING_ASSETS_ID: String = "runner_running"
    const val RUNNER_DODGING_ASSETS_ID: String = "runner_dodging"
    const val RUNNER_HIT_ASSETS_ID: String = "runner_hit"
    const val RUNNER_JUMPING_ASSETS_ID: String = "runner_jumping"
    const val RUNNING_SMALL_ENEMY_ASSETS_ID: String = "running_small_enemy"
    const val RUNNING_LONG_ENEMY_ASSETS_ID: String = "running_long_enemy"
    const val RUNNING_BIG_ENEMY_ASSETS_ID: String = "running_big_enemy"
    const val RUNNING_WIDE_ENEMY_ASSETS_ID: String = "running_wide_enemy"
    const val FLYING_SMALL_ENEMY_ASSETS_ID: String = "flying_small_enemy"
    const val FLYING_WIDE_ENEMY_ASSETS_ID: String = "flying_wide_enemy"

    const val BACKGROUND_IMAGE_PATH: String = "background.png"
    const val GROUND_IMAGE_PATH: String = "ground.png"
    const val SPRITES_ATLAS_PATH: String = "sprites.txt"
    val RUNNER_RUNNING_REGION_NAMES = arrayOf("alienBeige_run1", "alienBeige_run2")
    const val RUNNER_DODGING_REGION_NAME: String = "alienBeige_dodge"
    const val RUNNER_HIT_REGION_NAME: String = "alienBeige_hit"
    const val RUNNER_JUMPING_REGION_NAME: String = "alienBeige_jump"

    val RUNNING_SMALL_ENEMY_REGION_NAMES = arrayOf("ladyBug_walk1", "ladyBug_walk2")
    val RUNNING_LONG_ENEMY_REGION_NAMES = arrayOf("barnacle_bite1", "barnacle_bite2")
    val RUNNING_BIG_ENEMY_REGION_NAMES = arrayOf("spider_walk1", "spider_walk2")
    val RUNNING_WIDE_ENEMY_REGION_NAMES = arrayOf("worm_walk1", "worm_walk2")
    val FLYING_SMALL_ENEMY_REGION_NAMES = arrayOf("bee_fly1", "bee_fly2")
    val FLYING_WIDE_ENEMY_REGION_NAMES = arrayOf("fly_fly1", "fly_fly2")

    const val SOUND_ON_REGION_NAME: String = "sound_on"
    const val SOUND_OFF_REGION_NAME: String = "sound_off"
    const val MUSIC_ON_REGION_NAME: String = "music_on"
    const val MUSIC_OFF_REGION_NAME: String = "music_off"
    const val PAUSE_REGION_NAME: String = "pause"
    const val PLAY_REGION_NAME: String = "play"
    const val BIG_PLAY_REGION_NAME: String = "play_big"
    const val LEADERBOARD_REGION_NAME: String = "leaderboard"
    const val ABOUT_REGION_NAME: String = "about"
    const val CLOSE_REGION_NAME: String = "close"
    const val SHARE_REGION_NAME: String = "share"
    const val ACHIEVEMENTS_REGION_NAME: String = "star"

    const val TUTORIAL_LEFT_REGION_NAME: String = "tutorial_left"
    const val TUTORIAL_RIGHT_REGION_NAME: String = "tutorial_right"
    const val TUTORIAL_LEFT_TEXT: String = "\nTap left to dodge"
    const val TUTORIAL_RIGHT_TEXT: String = "\nTap right to jump"

    const val RUNNER_JUMPING_SOUND: String = "jump.wav"
    const val RUNNER_HIT_SOUND: String = "hit.wav"
    const val GAME_MUSIC: String = "fun_in_a_bottle.mp3"

    const val FONT_NAME: String = "roboto_bold.ttf"

    const val ABOUT_TEXT: String = "Developed by: @gamestudio24\nPowered by: " +
            "@libgdx\nGraphics: @kenneywings\nMusic: @kmacleod"
    const val PAUSED_LABEL: String = "Paused"
}
