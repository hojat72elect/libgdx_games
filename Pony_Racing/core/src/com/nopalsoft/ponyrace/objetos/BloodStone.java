package com.nopalsoft.ponyrace.objetos;

import java.util.Random;

public class BloodStone extends GameObject {
    public float lastStatetime;
    public float stateTime;
    public Tipo tipo;

    public BloodStone(float x, float y, Tipo tipo, Random oRan) {
        super(x, y);
        stateTime = oRan.nextFloat() * 5f;
        lastStatetime = stateTime;
        this.tipo = tipo;
    }

    public void update(float delta) {
        lastStatetime = stateTime;
        stateTime += delta;
    }

    public enum Tipo {
        chica, mediana, grande
    }
}
