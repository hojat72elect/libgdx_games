package com.nopalsoft.zombiekiller.game_objects;

import com.badlogic.gdx.math.Vector2;

public class Platform {

    public final Vector2 position;

    public final float width, height;

    public Platform(float x, float y, float width, float height) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }
}
