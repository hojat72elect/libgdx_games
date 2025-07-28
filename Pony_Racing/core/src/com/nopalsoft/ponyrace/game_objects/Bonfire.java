package com.nopalsoft.ponyrace.game_objects;

import java.util.Random;

public class Bonfire extends BaseGameObject {

    public float lastStateTime;
    public float stateTime;

    public Bonfire(float x, float y, Random oRan) {
        super(x, y);
        stateTime = oRan.nextFloat() * 5f;
        lastStateTime = stateTime;
    }

    public void update(float delta) {
        lastStateTime = stateTime;
        stateTime += delta;
    }
}
