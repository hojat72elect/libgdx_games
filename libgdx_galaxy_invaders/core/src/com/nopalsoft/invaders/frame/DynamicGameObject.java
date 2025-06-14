package com.nopalsoft.invaders.frame;

import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;

    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
    }

    public DynamicGameObject(float x, float y, float radio) {
        super(x, y, radio);
        velocity = new Vector2();
    }
}
