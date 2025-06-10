package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PowerUpItem implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_TAKEN = 1;
    public int state;

    public final static float DRAW_WIDTH = .27f;
    public final static float DRAW_HEIGHT = .34f;
    public final static float WIDTH = .25f;
    public final static float HEIGHT = .32f;

    public final static int TYPE_JETPACK = 0;
    public final static int TYPE_BUBBLE = 1;
    public final static int TYPE_GUN = 2;
    public int type;

    public final Vector2 position;

    public float stateTime;

    public PowerUpItem() {
        position = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        state = STATE_NORMAL;
        stateTime = 0;

        type = MathUtils.random(2);
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void take() {
        state = STATE_TAKEN;
        stateTime = 0;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
}
