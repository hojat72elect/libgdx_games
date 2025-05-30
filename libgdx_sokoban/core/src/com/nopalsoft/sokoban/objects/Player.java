package com.nopalsoft.sokoban.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.Settings;

public class Player extends Tiles {
    public static int STATE_LEFT = 0;
    public static int STATE_UP = 1;
    public static int STATE_DOWN = 2;
    public static int STATE_RIGHT = 3;
    public static int STATE_STAND = 4;
    int state;

    float stateTime;

    public Player(int posicion) {
        super(posicion);
        state = STATE_STAND;
        stateTime = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    public void moveToPosition(int pos, boolean up, boolean down, boolean right, boolean left) {
        super.moveToPosition(pos, false);

        if (up) {
            state = STATE_UP;
        } else if (down) {
            state = STATE_DOWN;
        } else if (right) {
            state = STATE_RIGHT;
        } else if (left) {
            state = STATE_LEFT;
        }
        stateTime = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        TextureRegion keyFrame;
        if (Settings.animationWalkIsON) {
            if (state == STATE_DOWN) {
                keyFrame = Assets.playerDownAnimation.getKeyFrame(stateTime, true);
            } else if (state == STATE_UP) {
                keyFrame = Assets.playerUpAnimation.getKeyFrame(stateTime, true);
            } else if (state == STATE_LEFT) {
                keyFrame = Assets.playerLeftAnimation.getKeyFrame(stateTime, true);
            } else if (state == STATE_RIGHT) {
                keyFrame = Assets.playerRightAnimation.getKeyFrame(stateTime, true);
            } else {
                keyFrame = Assets.playerStand;
            }
        } else {
            keyFrame = Assets.playerStand;
        }

        batch.draw(keyFrame, getX(), getY(), SIZE, SIZE);
    }

    @Override
    protected void onMovementToPositionCompleted() {
        state = STATE_STAND;
        stateTime = 0;
    }

    public boolean canMove() {
        return state == STATE_STAND;
    }
}
