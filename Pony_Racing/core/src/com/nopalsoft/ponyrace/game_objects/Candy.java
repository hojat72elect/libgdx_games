package com.nopalsoft.ponyrace.game_objects;

import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class Candy extends BaseGameObject {
    public static float PICK_UP_DURATION; // if you don't pick them up before this time ends, they will be deleted
    public float lastStateTime;
    public float stateTime;
    public State state;
    public Skeleton skeleton;

    public Candy(float x, float y, TileMapHandler oWorld) {
        super(x, y);
        stateTime = oWorld.random.nextFloat() * 5f;
        lastStateTime = stateTime;
        state = State.NORMAL;
        skeleton = new Skeleton(oWorld.game.assetsHandler.dulceSkeletonData);
        PICK_UP_DURATION = oWorld.game.assetsHandler.dulceTomadaAnim.getDuration();
    }

    public void update(float delta) {
        lastStateTime = stateTime;
        stateTime += delta;
    }

    public void hitPony() {
        state = State.ACTIVE;
        stateTime = 0;
    }

    public enum State {
        NORMAL,
        ACTIVE
    }
}
