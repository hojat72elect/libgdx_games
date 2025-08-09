package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.lander.Settings;

public class Nave {

    final public static float DRAW_WIDTH = .7f;
    final public static float DRAW_HEIGHT = 1.59f;
    final public static float WIDTH = .5f;
    final public static float HEIGHT = 1.0f;

    final public static float DENSIDAD_INICIAL = .7f;

    final public static float VELOCIDAD_FLY = 2f;
    final public static float MAX_SPEED_Y = 2;
    final public static float MIN_SPEED_Y = -4;
    final public static float VELOCIDAD_MOVE = 1.3f;
    final public static float MAX_SPEED_X = 1f;
    final public static float GAS_INICIAL = 100;
    final public static float VIDA_INICIAL = 20;
    public static int STATE_NORMAL = 0;
    public static int STATE_EXPLODE = 1;
    public static float EXPLODE_TIME = .05f * 20;
    public static float TIME_HURT_BY_BOMB = .05f;// Debe ser un numero pequeno
    public float velocidadFly;
    public float velocidadMove;
    public float gas;
    public float vida;
    public Vector2 position;
    public Vector2 velocity;
    public float angleRad;

    public int state;
    public float stateTime;

    public boolean isFlying;
    public boolean isHurtByBomb;

    /**
     * Cuando aterrizo en el area de ganar el juego
     */
    public boolean isLanded;

    public Nave(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_NORMAL;
        gas = GAS_INICIAL;
        vida = VIDA_INICIAL;
        velocidadFly = VELOCIDAD_FLY;
        velocidadMove = VELOCIDAD_MOVE;
        isFlying = false;

        // Upgrades
        velocidadFly += (.09f * Settings.nivelVelocidadY);
        velocidadMove += (.02f * Settings.nivelRotacion);
        vida += (5.3f * Settings.nivelVida);
        gas += (33.3f * Settings.nivelGas);
    }

    public void update(float delta, Body body, float accelX, float accelY) {

        if (state == STATE_NORMAL) {

            if (gas < 0 || accelY == 0) {
                accelX = accelY = 0;
                isFlying = false;
            } else
                isFlying = true;

            // I put the speed on
            body.applyForceToCenter(velocidadMove * accelX, velocidadFly * accelY, true);

            // I put the speed in x to the opponent so that it reduces its speed
            body.applyForceToCenter(body.getLinearVelocity().x * -.015f, 0, true);

            velocity = body.getLinearVelocity();

            if (isHurtByBomb && stateTime > TIME_HURT_BY_BOMB)
                isHurtByBomb = false;

            if (!isHurtByBomb) {
                if (velocity.y > Nave.MAX_SPEED_Y) {
                    velocity.y = Nave.MAX_SPEED_Y;
                    body.setLinearVelocity(velocity);
                } else if (velocity.y < MIN_SPEED_Y) {
                    velocity.y = MIN_SPEED_Y;
                    body.setLinearVelocity(velocity);
                }
                if (velocity.x > Nave.MAX_SPEED_X) {
                    velocity.x = Nave.MAX_SPEED_X;
                    body.setLinearVelocity(velocity);
                } else if (velocity.x < -Nave.MAX_SPEED_X) {
                    velocity.x = -Nave.MAX_SPEED_X;
                    body.setLinearVelocity(velocity);
                }
            }

            angleRad = MathUtils.atan2(-accelX, accelY);

            position.x = body.getPosition().x;
            position.y = body.getPosition().y;

            int MAX_ANGLE_DEGREES = 35;
            float angleLimitRad = (float) Math.toRadians(MAX_ANGLE_DEGREES);

            if (angleRad > angleLimitRad)
                angleRad = angleLimitRad;
            else if (angleRad < -angleLimitRad)
                angleRad = -angleLimitRad;

            body.setTransform(position.x, position.y, angleRad);

            if (accelX != 0 || accelY != 0)
                gas -= (5 * delta);
        } else {
            body.setLinearVelocity(0, 0);
        }

        stateTime += delta;
    }

    public void colision(float fuerzaImpacto) {
        if (state == STATE_NORMAL) {
            vida -= fuerzaImpacto;
            if (vida <= 0) {

                state = STATE_EXPLODE;
                stateTime = 0;
            }
        }
    }

    public void getHurtByLaser(float dano) {
        isHurtByBomb = true;
        stateTime = 0;
        colision(dano);
    }

    public void getHurtByBomb(float dano) {
        isHurtByBomb = true;
        stateTime = 0;
        colision(dano);
    }
}
