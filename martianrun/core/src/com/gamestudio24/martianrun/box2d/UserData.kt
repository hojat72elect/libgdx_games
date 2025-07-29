package com.gamestudio24.martianrun.box2d

import com.gamestudio24.martianrun.enums.UserDataType

abstract class UserData(@JvmField var width: Float, @JvmField var height: Float) {
    @JvmField
    var userDataType: UserDataType? = null
}
