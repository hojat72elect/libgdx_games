package com.nopalsoft.ponyrace.game_objects;

import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class Balloons extends BaseGameObject {
    public static float TAKEN_ANIMATION_DURATION;
    public float lastStateTime;
    public float stateTime;
    public State state;
    public Skeleton balloonsSkeleton;

    public Balloons(float x, float y, TileMapHandler tileMapHandler) {
        super(x, y);
        stateTime = tileMapHandler.random.nextFloat() * 5f;
        lastStateTime = stateTime;
        state = State.IDLE;
        balloonsSkeleton = new Skeleton(tileMapHandler.game.assetsHandler.globoSkeletonData);
        TAKEN_ANIMATION_DURATION = tileMapHandler.game.assetsHandler.globoTomadaAnim.getDuration();
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
