package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Platform implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_DESTROY = 1;
    public int state;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_BREAKABLE = 1;
    public static final float DRAW_HEIGHT_NORMAL = .45f;

    public static final float DRAW_WIDTH_NORMAL = 1.25f;
    public static final float HEIGHT_NORMAL = .45f;
    public static final float WIDTH_NORMAL = 1.25f;
    public int type;

    public static final int COLOR_BEIGE = 0;
    public static final int COLOR_BLUE = 1;
    public static final int COLOR_GRAY = 2;
    public static final int COLOR_GREEN = 3;
    public static final int COLOR_MULTICOLOR = 4;
    public static final int COLOR_PINK = 5;
    public static final int COLOR_BEIGE_LIGHT = 6;
    public static final int COLOR_BLUE_LIGHT = 7;
    public static final int COLOR_GRAY_LIGHT = 8;
    public static final int COLOR_GREEN_LIGHT = 9;
    public static final int COLOR_MULTICOLOR_LIGHT = 10;
    public static final int COLOR_PINK_LIGHT = 11;
    public int color;

    public final Vector2 position;
    public float stateTime;

    public Platform() {
        position = new Vector2();
    }

    public void initialize(float x, float y, int platformType) {
        position.set(x, y);
        this.type = platformType;

        if (platformType == TYPE_NORMAL) {
            color = MathUtils.random(11);
        } else {
            color = MathUtils.random(5);
        }
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
