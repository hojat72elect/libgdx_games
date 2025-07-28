package com.gamestudio24.martianrun.box2d;

import com.badlogic.gdx.math.Vector2;
import com.gamestudio24.martianrun.enums.UserDataType;
import com.gamestudio24.martianrun.utils.Constants;

public class EnemyUserData extends UserData {

    private final String animationAssetId;
    private Vector2 linearVelocity;

    public EnemyUserData(float width, float height, String animationAssetId) {
        super(width, height);
        userDataType = UserDataType.ENEMY;
        linearVelocity = Constants.ENEMY_LINEAR_VELOCITY;
        this.animationAssetId = animationAssetId;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public String getAnimationAssetId() {
        return animationAssetId;
    }
}
