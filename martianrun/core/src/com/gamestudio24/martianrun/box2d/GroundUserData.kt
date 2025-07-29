package com.gamestudio24.martianrun.box2d

import com.gamestudio24.martianrun.enums.UserDataType

class GroundUserData(width: Float, height: Float) : UserData(width, height) {
    init {
        userDataType = UserDataType.GROUND
    }
}