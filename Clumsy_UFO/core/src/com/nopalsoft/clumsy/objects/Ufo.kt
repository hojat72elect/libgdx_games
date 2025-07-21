package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Ufo {

    public static final float HURT_DURATION = .5f;
    public static final float DEATH_DURATION = .75f;
    public static float JUMP_SPEED = 5;
    public static int STATE_NORMAL = 0;
    public static int STATE_HURT = 1;
    public static int STATE_DEAD = 2;
    public Vector2 position;

    public int state;
    public float stateTime;

    public float angleRad;

    public Ufo(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_NORMAL;
    }

    public void update(float delta, Body body) {

        if (body != null) {
            position.x = body.getPosition().x;
            position.y = body.getPosition().y;

            Vector2 velocity = body.getLinearVelocity();

            angleRad = MathUtils.atan2(-.1f, velocity.y);
            float angleLimitRad;

            int MAX_ANGLE_DEGREES = 15;
            angleLimitRad = (float) Math.toRadians(MAX_ANGLE_DEGREES);

            if (angleRad > angleLimitRad)
                angleRad = angleLimitRad;
            else if (angleRad < -angleLimitRad)
                angleRad = -angleLimitRad;
        }

        stateTime += delta;
    }

    public void getHurt() {
        state = STATE_HURT;
        stateTime = 0;
    }

    public void die() {
        state = STATE_DEAD;
        stateTime = 0;
    }
}
