package com.nopalsoft.ponyrace.game_objects;

import java.util.Random;

public class Pluma extends GameObject {
    public float lastStatetime;
    public float stateTime;
    public State state;


    public Pluma(float x, float y, Random oRan) {
        super(x, y);
        stateTime = oRan.nextFloat() * 5f;
        lastStatetime = stateTime;
        state = State.normal;
    }

    public void update(float delta) {
        lastStatetime = stateTime;
        stateTime += delta;
    }

    public enum State {
        normal, tomada
    }
}
