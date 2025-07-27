package com.nopalsoft.ponyrace.game_objects;

import java.util.Random;

public class BloodStone extends GameObject {
    public float lastStateTime;
    public float stateTime;
    public Type type;

    public BloodStone(float x, float y, Type type, Random oRan) {
        super(x, y);
        stateTime = oRan.nextFloat() * 5f;
        lastStateTime = stateTime;
        this.type = type;
    }

    public void update(float delta) {
        lastStateTime = stateTime;
        stateTime += delta;
    }

    public enum Type {
        chica, mediana, grande
    }
}
