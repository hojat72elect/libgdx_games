package com.nopalsoft.ponyrace.game_objects;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;

public class BaseGameObject {
    public final Vector3 position;
    public final Polygon bounds;


    public BaseGameObject(float x, float y, float z, float[] vertices) {
        this.position = new Vector3(x, y, z);
        this.bounds = new Polygon(vertices);
    }

    public BaseGameObject(float x, float y) {
        this.position = new Vector3(x, y, 0);
        this.bounds = new Polygon(new float[]{0, 0, 0, 0, 0, 0});
    }
}
