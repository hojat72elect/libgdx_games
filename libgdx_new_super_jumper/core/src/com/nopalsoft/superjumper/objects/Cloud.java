package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.superjumper.screens.Screens;

/**
 * Clouds are indestructible. They all start out happy until you shoot them.
 */
public class Cloud implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_DEAD = 1;
    public int state;

    public final static float DRAW_WIDTH = .95f;
    public final static float DRAW_HEIGHT = .6f;

    public final static float WIDTH = .65f;
    public final static float HEIGHT = .4f;

    public final static float SPEED_X = .5f;

    public final static int TYPE_HAPPY = 0;
    public final static int TYPE_ANGRY = 1;
    public final static float TIME_UNTIL_LIGHTNING = 5;

    public final static float TIME_TO_BLOW = 2;
    public float timeToBlow;

    public final static float DURATION_BLOW = 3;
    public float durationBlow;
    public int type;
    public float timeUntilLightning;

    final public Vector2 position;
    public Vector2 speed;

    public boolean isBlowing;
    public boolean isLightning;

    public float stateTime;

    public Cloud() {
        position = new Vector2();
        speed = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        speed.set(0, 0);// I set the speed from the method where I create it.
        stateTime = 0;
        state = STATE_NORMAL;
        type = TYPE_HAPPY;

        isBlowing = isLightning = false;

        timeToBlow = durationBlow = 0;
        timeUntilLightning = MathUtils.random(TIME_UNTIL_LIGHTNING);
    }

    public void update(Body body, float delta) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        speed = body.getLinearVelocity();

        if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
            speed.x = speed.x * -1;
        }

        body.setLinearVelocity(speed);
        speed = body.getLinearVelocity();

        if (type == TYPE_ANGRY) {
            timeToBlow += delta;
            if (!isBlowing && timeToBlow >= TIME_TO_BLOW) {
                if (MathUtils.randomBoolean())
                    isBlowing = true;
                timeToBlow = 0;
            }

            if (isBlowing) {
                durationBlow += delta;
                if (durationBlow >= DURATION_BLOW) {
                    durationBlow = 0;
                    isBlowing = false;
                }
            }
        } else {// TYPE HAPPY

            if (!isLightning) {
                timeUntilLightning += delta;
                if (timeUntilLightning >= TIME_UNTIL_LIGHTNING) {
                    isLightning = true;
                }
            }
        }

        stateTime += delta;
    }

    public void fireLighting() {
        isLightning = false;
        timeUntilLightning = MathUtils.random(TIME_UNTIL_LIGHTNING);
    }

    public void hit() {
        if (type == TYPE_HAPPY) {
            type = TYPE_ANGRY;
            stateTime = timeToBlow = durationBlow = 0;
        }
    }

    public void destroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD;
            stateTime = 0;
        }
    }

    @Override
    public void reset() {
    }
}
