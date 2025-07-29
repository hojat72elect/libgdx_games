package com.gamestudio24.martianrun.utils

import com.badlogic.gdx.physics.box2d.Body
import com.gamestudio24.martianrun.box2d.UserData
import com.gamestudio24.martianrun.enums.UserDataType

object BodyUtils {
    fun bodyInBounds(body: Body): Boolean {
        val userData = body.userData as UserData

        return when (userData.userDataType) {
            UserDataType.RUNNER, UserDataType.ENEMY -> body.getPosition().x + userData.width / 2 > 0
            else -> true
        }
    }

    fun bodyIsEnemy(body: Body): Boolean {
        val userData = body.userData as UserData?

        return userData != null && userData.userDataType == UserDataType.ENEMY
    }

    fun bodyIsRunner(body: Body): Boolean {
        val userData = body.userData as UserData?

        return userData != null && userData.userDataType == UserDataType.RUNNER
    }

    fun bodyIsGround(body: Body): Boolean {
        val userData = body.userData as UserData?

        return userData != null && userData.userDataType == UserDataType.GROUND
    }
}
