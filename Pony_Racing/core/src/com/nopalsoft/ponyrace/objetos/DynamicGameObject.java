package com.nopalsoft.ponyrace.objetos;

import com.badlogic.gdx.math.Vector3;

public class DynamicGameObject extends GameObject {
    public final Vector3 velocity;

    public DynamicGameObject(float x, float y, float z, float[] vertices) {
        super(x, y, z, vertices);
        velocity = new Vector3();
    }
}
