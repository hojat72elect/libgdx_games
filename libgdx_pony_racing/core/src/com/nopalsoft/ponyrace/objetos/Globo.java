package com.nopalsoft.ponyrace.objetos;

import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.game.WorldTiled;

public class Globo extends GameObject {
    public enum State {
        normal,
        tomada
    }

    public static float TIEMPO_TOMADA;

    public float lastStatetime;
    public float stateTime;

    public State state;
    public Skeleton objSkeleton;

    public Globo(float x, float y, WorldTiled oWorld) {
        super(x, y, 0);
        stateTime = oWorld.oRan.nextFloat() * 5f;
        lastStatetime = stateTime;
        state = State.normal;
        objSkeleton = new Skeleton(oWorld.game.oAssets.globoSkeletonData);
        TIEMPO_TOMADA = oWorld.game.oAssets.globoTomadaAnim.getDuration();
    }

    public void update(float delta) {
        lastStatetime = stateTime;
        stateTime += delta;
    }

    public void hitPony() {
        state = State.tomada;
        stateTime = 0;
    }
}
