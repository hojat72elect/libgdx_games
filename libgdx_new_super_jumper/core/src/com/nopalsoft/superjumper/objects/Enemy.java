package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.superjumper.screens.Screens;

public class Enemy implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_DEAD = 1;
    public int state;

    public final static float DRAW_WIDTH = .95f;
    public final static float DRAW_HEIGHT = .6f;

    public final static float WIDTH = .65f;
    public final static float HEIGHT = .4f;

    public final static float SPEED_X = 2;

    final public Vector2 position;
    public Vector2 speed;
    public float angleDegree;

    public float stateTime;

    public Enemy() {
        position = new Vector2();
        speed = new Vector2();
    }

    public void initialize(float x, float y) {
        position.set(x, y);
        speed.set(0, 0); // I set the speed from the method where I create it.
        stateTime = 0;
        state = STATE_NORMAL;
        angleDegree = 0;
    }

    public void update(Body body, float delta) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        speed = body.getLinearVelocity();

        if (state == STATE_NORMAL) {

            if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
                speed.x = speed.x * -1;
            }
        } else {
            body.setAngularVelocity(MathUtils.degRad * 360);
            if (speed.y < -5)
                speed.y = -5;
        }

        body.setLinearVelocity(speed);

        angleDegree = body.getAngle() * MathUtils.radDeg;

        speed = body.getLinearVelocity();
        stateTime += delta;
    }

    public void hit() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD;
            stateTime = 0;
        }
    }

    @Override
    public void reset() {
    }
}
