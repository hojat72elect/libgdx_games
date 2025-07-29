package com.gamestudio24.martianrun.utils

import com.badlogic.gdx.math.Vector2

object Constants {
    const val GAME_NAME = "Martian Run!"

    const val APP_WIDTH: Int = 800
    const val APP_HEIGHT: Int = 480
    const val WORLD_TO_SCREEN = 32f

    val WORLD_GRAVITY: Vector2 = Vector2(0f, -10f)

    const val GROUND_X = 0f
    const val GROUND_Y = 0f
    const val GROUND_WIDTH = 25f
    const val GROUND_HEIGHT = 2f
    const val GROUND_DENSITY = 0f

    const val RUNNER_X = 2f

    const val RUNNER_Y = GROUND_Y + GROUND_HEIGHT
    const val RUNNER_WIDTH = 1f
    const val RUNNER_HEIGHT = 2f
    const val RUNNER_GRAVITY_SCALE = 3f
    const val RUNNER_DENSITY = 0.5f
    const val RUNNER_DODGE_X = 2f
    const val RUNNER_DODGE_Y = 1.5f

    val RUNNER_JUMPING_LINEAR_IMPULSE: Vector2 = Vector2(0f, 13f)
    const val RUNNER_HIT_ANGULAR_IMPULSE = 10f

    val ENEMY_LINEAR_VELOCITY: Vector2 = Vector2(-10f, 0f)

    const val BACKGROUND_ASSETS_ID = "background"
    const val GROUND_ASSETS_ID = "ground"
    const val RUNNER_RUNNING_ASSETS_ID = "runner_running"
    const val RUNNER_DODGING_ASSETS_ID = "runner_dodging"
    const val RUNNER_HIT_ASSETS_ID = "runner_hit"
    const val RUNNER_JUMPING_ASSETS_ID = "runner_jumping"
    const val RUNNING_SMALL_ENEMY_ASSETS_ID = "running_small_enemy"
    const val RUNNING_LONG_ENEMY_ASSETS_ID = "running_long_enemy"
    const val RUNNING_BIG_ENEMY_ASSETS_ID = "running_big_enemy"
    const val RUNNING_WIDE_ENEMY_ASSETS_ID = "running_wide_enemy"
    const val FLYING_SMALL_ENEMY_ASSETS_ID = "flying_small_enemy"
    const val FLYING_WIDE_ENEMY_ASSETS_ID = "flying_wide_enemy"

    const val BACKGROUND_IMAGE_PATH = "background.png"
    const val GROUND_IMAGE_PATH = "ground.png"
    const val SPRITES_ATLAS_PATH = "sprites.txt"
    val RUNNER_RUNNING_REGION_NAMES = arrayOf("alienBeige_run1", "alienBeige_run2")
    const val RUNNER_DODGING_REGION_NAME = "alienBeige_dodge"
    const val RUNNER_HIT_REGION_NAME = "alienBeige_hit"
    const val RUNNER_JUMPING_REGION_NAME = "alienBeige_jump"

    val RUNNING_SMALL_ENEMY_REGION_NAMES = arrayOf("ladyBug_walk1", "ladyBug_walk2")
    val RUNNING_LONG_ENEMY_REGION_NAMES = arrayOf("barnacle_bite1", "barnacle_bite2")
    val RUNNING_BIG_ENEMY_REGION_NAMES = arrayOf("spider_walk1", "spider_walk2")
    val RUNNING_WIDE_ENEMY_REGION_NAMES = arrayOf("worm_walk1", "worm_walk2")
    val FLYING_SMALL_ENEMY_REGION_NAMES = arrayOf("bee_fly1", "bee_fly2")
    val FLYING_WIDE_ENEMY_REGION_NAMES = arrayOf("fly_fly1", "fly_fly2")

    const val SOUND_ON_REGION_NAME = "sound_on"
    const val SOUND_OFF_REGION_NAME = "sound_off"
    const val MUSIC_ON_REGION_NAME = "music_on"
    const val MUSIC_OFF_REGION_NAME = "music_off"
    const val PAUSE_REGION_NAME = "pause"
    const val PLAY_REGION_NAME = "play"
    const val BIG_PLAY_REGION_NAME = "play_big"
    const val LEADERBOARD_REGION_NAME = "leaderboard"
    const val ABOUT_REGION_NAME = "about"
    const val CLOSE_REGION_NAME = "close"
    const val SHARE_REGION_NAME = "share"
    const val ACHIEVEMENTS_REGION_NAME = "star"

    const val TUTORIAL_LEFT_REGION_NAME = "tutorial_left"
    const val TUTORIAL_RIGHT_REGION_NAME = "tutorial_right"
    const val TUTORIAL_LEFT_TEXT = "\nTap left to dodge"
    const val TUTORIAL_RIGHT_TEXT = "\nTap right to jump"

    const val RUNNER_JUMPING_SOUND = "jump.wav"
    const val RUNNER_HIT_SOUND = "hit.wav"
    const val GAME_MUSIC = "fun_in_a_bottle.mp3"

    const val FONT_NAME = "roboto_bold.ttf"

    const val ABOUT_TEXT = "Developed by: @gamestudio24\nPowered by: @libgdx\nGraphics: @kenneywings\nMusic: @kmacleod"
    const val PAUSED_LABEL = "Paused"
}
