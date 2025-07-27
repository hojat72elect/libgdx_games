package com.nopalsoft.ponyrace.objetos;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
    public final Vector3 position;
    public final Polygon bounds;


    public GameObject(float x, float y, float z, float[] vertices) {
        this.position = new Vector3(x, y, z);
        this.bounds = new Polygon(vertices);
    }

    public GameObject(float x, float y, float z) {
        this.position = new Vector3(x, y, 0);
        this.bounds = new Polygon(new float[]{0, 0, 0, 0, 0, 0});
    }
}
