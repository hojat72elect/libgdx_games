package com.nopalsoft.ninjarunner.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Wall implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_DESTROY = 1;
    public int state;

    public static final float WIDTH = .98f;
    public static final float HEIGHT = 4.30f;

    public final Vector2 position;
    public float stateTime;

    public Wall() {
        position = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        state = STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void setDestroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY;
            stateTime = 0;
        }
    }

    @Override
    public void reset() {
    }
}
