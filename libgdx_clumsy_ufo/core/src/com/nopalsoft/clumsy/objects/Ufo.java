package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Ufo {

    public static float VELOCIDAD_JUMP = 5;
    final private int MAX_ANGLE_DEGREES = 15;// 90

    public static int STATE_NORMAL = 0;
    public static int STATE_HURT = 1;
    public static int STATE_DEAD = 2;

    public static final float TIEMPO_HURT = .5f;
    public static final float TIEMPO_MUERTO = .75f;

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

            // if (state == STATE_NORMAL)
            angleLimitRad = (float) Math.toRadians(MAX_ANGLE_DEGREES);
            // else
            // angleLimitRad = (float) Math.toRadians(90);

            if (angleRad > angleLimitRad)
                angleRad = angleLimitRad;
            else if (angleRad < -angleLimitRad)
                angleRad = -angleLimitRad;

            // Gdx.app.log("Angulo", "Angulo " + Math.toDegrees(angleRad) + " Velocidad y " + velocity.y);
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
