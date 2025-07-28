package com.nopalsoft.ponyrace.game_objects;

import java.util.Random;

public class Wing extends BaseGameObject {
    public float lastStateTime;
    public float stateTime;
    public State state;


    public Wing(float x, float y, Random oRan) {
        super(x, y);
        stateTime = oRan.nextFloat() * 5f;
        lastStateTime = stateTime;
        state = State.IDLE;
    }

    public void update(float delta) {
        lastStateTime = stateTime;
        stateTime += delta;
    }

    public enum State {
        IDLE, ACTIVE
    }
}
