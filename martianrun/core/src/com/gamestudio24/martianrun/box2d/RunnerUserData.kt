package com.gamestudio24.martianrun.box2d

import com.badlogic.gdx.math.Vector2
import com.gamestudio24.martianrun.enums.UserDataType
import com.gamestudio24.martianrun.utils.Constants

class RunnerUserData(width: Float, height: Float) : UserData(width, height) {
    val runningPosition: Vector2 = Vector2(Constants.RUNNER_X, Constants.RUNNER_Y)
    val dodgePosition: Vector2 = Vector2(Constants.RUNNER_DODGE_X, Constants.RUNNER_DODGE_Y)
    var jumpingLinearImpulse: Vector2? = Constants.RUNNER_JUMPING_LINEAR_IMPULSE

    init {
        userDataType = UserDataType.RUNNER
    }

    // In radians
    val dodgeAngle = (-90F * (Math.PI / 180f)).toFloat()

    val hitAngularImpulse = Constants.RUNNER_HIT_ANGULAR_IMPULSE
}
