package com.nopalsoft.ponyrace.objetos;

import java.util.Random;

public class Pluma extends GameObject {
    public enum State {
        normal, tomada
    }

    public float lastStatetime;
    public float stateTime;


    public State state;

    public Pluma(float x, float y, Random oRan) {
        super(x, y, 0);
        stateTime = oRan.nextFloat() * 5f;
        lastStatetime = stateTime;
        state = State.normal;
    }

    public void update(float delta) {
        lastStatetime = stateTime;
        stateTime += delta;
    }

    public void hitPony() {
        state = State.tomada;
    }
}
