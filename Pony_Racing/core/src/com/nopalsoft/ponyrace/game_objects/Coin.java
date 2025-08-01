package com.nopalsoft.ponyrace.game_objects;

import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class Coin extends BaseGameObject {
    public static float TIEMPO_TOMADA;
    public float lastStatetime;
    public float stateTime;
    public State state;
    public Skeleton coinSkeleton;

    public Coin(float x, float y, TileMapHandler tileMapHandler) {
        super(x, y);
        stateTime = tileMapHandler.random.nextFloat() * 5f;
        lastStatetime = stateTime;
        state = State.IDLE;
        coinSkeleton = new Skeleton(tileMapHandler.game.assetsHandler.skeletonMonedaData);
        TIEMPO_TOMADA = tileMapHandler.game.assetsHandler.monedaTomadaAnim.getDuration();
    }

    public void update(float delta) {
        lastStatetime = stateTime;
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
