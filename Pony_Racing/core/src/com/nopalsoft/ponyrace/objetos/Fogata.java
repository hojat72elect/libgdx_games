package com.nopalsoft.ponyrace.objetos;

import java.util.Random;

public class Fogata extends GameObject {

    public float lastStatetime;
    public float stateTime;

    public Fogata(float x, float y, Random oRan) {
        super(x, y);
        stateTime = oRan.nextFloat() * 5f;
        lastStatetime = stateTime;
    }

    public void update(float delta) {
        lastStatetime = stateTime;
        stateTime += delta;
    }
}
