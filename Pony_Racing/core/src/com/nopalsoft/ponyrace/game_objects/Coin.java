package com.nopalsoft.ponyrace.game_objects;

import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class Coin extends BaseGameObject {
    public static float TIEMPO_TOMADA;
    public float lastStatetime;
    public float stateTime;
    public State state;
    public Skeleton monedaSkeleton;

    public Coin(float x, float y, TileMapHandler oWorld) {
        super(x, y);
        stateTime = oWorld.random.nextFloat() * 5f;
        lastStatetime = stateTime;
        state = State.normal;
        monedaSkeleton = new Skeleton(oWorld.game.assetsHandler.skeletonMonedaData);
        TIEMPO_TOMADA = oWorld.game.assetsHandler.monedaTomadaAnim.getDuration();
    }

    public void update(float delta) {
        lastStatetime = stateTime;
        stateTime += delta;
    }

    public void hitPony() {
        state = State.tomada;
        stateTime = 0;
    }

    public enum State {
        normal,
        tomada
    }
}
