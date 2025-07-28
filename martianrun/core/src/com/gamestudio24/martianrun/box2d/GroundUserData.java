package com.gamestudio24.martianrun.box2d;

import com.gamestudio24.martianrun.enums.UserDataType;

public class GroundUserData extends UserData {

    public GroundUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.GROUND;
    }
}