package com.nopalsoft.ponyrace.game_objects;

import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class Chili extends BaseGameObject {
    public static float HURT_DURATION = 2f;
    public static float HIT_ANIMATION_DURATION;
    public float lastStateTime;
    public float stateTime;
    public State state;
    public Skeleton chiliSkeleton;

    public Chili(float x, float y, TileMapHandler tileMapHandler) {
        super(x, y);
        stateTime = tileMapHandler.random.nextFloat() * 5f;
        lastStateTime = stateTime;
        state = State.IDLE;
        chiliSkeleton = new Skeleton(tileMapHandler.game.assetsHandler.chileSkeletonData);
        HIT_ANIMATION_DURATION = tileMapHandler.game.assetsHandler.chileTomadaAnim.getDuration();
    }

    public void update(float delta) {
        lastStateTime = stateTime;
        stateTime += delta;
    }

    public void hitPony() {
        state = State.TAKEN;
        stateTime = 0;
    }

    public enum State {
        IDLE,
        TAKEN
    }
}
