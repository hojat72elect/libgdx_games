package com.nopalsoft.ninjarunner.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.ninjarunner.Assets;


public class Missile implements Poolable, Comparable<Missile> {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_EXPLODE = 1;
    public final static int STATE_DESTROY = 2;
    public int state;

    private final static float DURATION_EXPLOSION = Assets.explosionAnimation.animationDuration + .1f;

    public static final float WIDTH = 1.27f;
    public static final float HEIGHT = .44f;

    public static final float SPEED_X = -2.5f;

    public final Vector2 position;
    public float stateTime;
    public float distanceFromPlayer;

    public Missile() {
        position = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        state = STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float delta, Body body, Player oPlayer) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x;
            position.y = body.getPosition().y;
        }
        if (state == STATE_EXPLODE) {

            if (stateTime >= DURATION_EXPLOSION) {
                state = STATE_DESTROY;
                stateTime = 0;
            }
        }

        distanceFromPlayer = oPlayer.position.dst(position);
        stateTime += delta;
    }

    public void setHitTarget() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE;
            stateTime = 0;
        }
    }

    public void setDestroy() {
        if (state != STATE_DESTROY) {
            state = STATE_DESTROY;
            stateTime = 0;
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public int compareTo(Missile o2) {
        return Float.compare(distanceFromPlayer, o2.distanceFromPlayer);
    }
}
