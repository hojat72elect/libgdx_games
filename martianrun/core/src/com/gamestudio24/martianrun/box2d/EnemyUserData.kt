package com.gamestudio24.martianrun.box2d

import com.badlogic.gdx.math.Vector2
import com.gamestudio24.martianrun.enums.UserDataType
import com.gamestudio24.martianrun.utils.Constants

class EnemyUserData(width: Float, height: Float, @JvmField val animationAssetId: String?) : UserData(width, height) {

    @JvmField
    var linearVelocity: Vector2? = Constants.ENEMY_LINEAR_VELOCITY

    init {
        userDataType = UserDataType.ENEMY
    }
}
