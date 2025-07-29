package com.gamestudio24.martianrun.enums

import com.gamestudio24.martianrun.utils.Constants

enum class EnemyType(@JvmField val width: Float, @JvmField val height: Float, @JvmField val x: Float, @JvmField val y: Float, @JvmField val density: Float, animationAssetsId: String) {
    RUNNING_SMALL(
        1f, 1f, Constants.ENEMY_X, Constants.RUNNING_SHORT_ENEMY_Y, Constants.ENEMY_DENSITY,
        Constants.RUNNING_SMALL_ENEMY_ASSETS_ID
    ),
    RUNNING_WIDE(
        2f, 1f, Constants.ENEMY_X, Constants.RUNNING_SHORT_ENEMY_Y, Constants.ENEMY_DENSITY,
        Constants.RUNNING_WIDE_ENEMY_ASSETS_ID
    ),
    RUNNING_LONG(
        1f, 2f, Constants.ENEMY_X, Constants.RUNNING_LONG_ENEMY_Y, Constants.ENEMY_DENSITY,
        Constants.RUNNING_LONG_ENEMY_ASSETS_ID
    ),
    RUNNING_BIG(
        2f, 2f, Constants.ENEMY_X, Constants.RUNNING_LONG_ENEMY_Y, Constants.ENEMY_DENSITY,
        Constants.RUNNING_BIG_ENEMY_ASSETS_ID
    ),
    FLYING_SMALL(
        1f, 1f, Constants.ENEMY_X, Constants.FLYING_ENEMY_Y, Constants.ENEMY_DENSITY,
        Constants.FLYING_SMALL_ENEMY_ASSETS_ID
    ),
    FLYING_WIDE(
        2f, 1f, Constants.ENEMY_X, Constants.FLYING_ENEMY_Y, Constants.ENEMY_DENSITY,
        Constants.FLYING_WIDE_ENEMY_ASSETS_ID
    );

    val animationAssetId: String?

    init {
        this.animationAssetId = animationAssetsId
    }
}
