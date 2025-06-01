package com.nopalsoft.sharkadventure.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;

public class Barrel implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_EXPLODE = 1;
    public final static int STATE_REMOVE = 2;
    public int state;

    public final static float EXPLOSION_DURATION = .1f * 8f;

    public final static float DRAW_WIDTH = .43f;
    public final static float DRAW_HEIGHT = .68f;

    public final static float WIDTH = .40f;
    public final static float HEIGHT = .65f;

    public final static int TYPE_YELLOW = 0;
    public final static int TYPE_BLACK = 1;
    public final static int TYPE_RED = 2;
    public final static int TYPE_GREEN = 3;
    public int type;

    final public Vector2 position;
    public float angleDegree;

    public float stateTime;

    public Barrel() {
        position = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        stateTime = 0;
        state = STATE_NORMAL;
        type = MathUtils.random(3);
    }

    public void update(Body body, float delta) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x;
            position.y = body.getPosition().y;
            angleDegree = MathUtils.radDeg * body.getAngle();

            if (position.y < -3)
                remove();
        } else if (state == STATE_EXPLODE && stateTime >= EXPLOSION_DURATION) {
            state = STATE_REMOVE;
            stateTime = 0;
        }

        stateTime += delta;
    }

    public void hit() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE;
            stateTime = 0;
            if (Settings.isSoundOn) {
                Assets.playExplosionSound();
            }
        }
    }

    public void remove() {
        state = STATE_REMOVE;
    }

    @Override
    public void reset() {
    }
}
