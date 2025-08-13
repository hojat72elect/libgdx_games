package com.nopalsoft.zombiewars.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Personajes {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_HURT = 1;
    public final static int STATE_ATTACK = 2;
    public final static int STATE_DEAD = 3;
    public final static int TIPO_RANGO = 0;
    public final static int TIPO_NO_RANGO = 1;
    public final float DURATION_HURT = .3f;
    public int state;
    public int tipo;
    public float DURATION_ATTACK;
    public float DURATION_DEAD;
    public int DAMAGE;
    public float DISTANCE_ATTACK;
    public Vector2 position;
    public float stateTime;
    public int vidas;
    public boolean attack;// solo puede herir una vez cada vez que atacca
    /**
     * Los buenos caminan a la derecha
     */
    public boolean isFacingLeft = false;
    protected float TIME_TO_ATTACK_AGAIN = 0;
    float VELOCIDAD_WALK;
    Body body;

    public Personajes(Body body) {
        this.body = body;
        position = new Vector2(body.getPosition().x, body.getPosition().y);

        state = STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float delta) {
        body.setAwake(true);
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        Vector2 velocity = body.getLinearVelocity();

        if (state == STATE_DEAD) {
            stateTime += delta;
            body.setLinearVelocity(0, velocity.y);
            return;
        } else if (state == STATE_HURT) {
            stateTime += delta;
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
            body.setLinearVelocity(0, velocity.y);
            return;
        } else if (state == STATE_ATTACK) {
            stateTime += delta;
            if (stateTime >= DURATION_ATTACK) {

                if ((stateTime - DURATION_ATTACK) >= TIME_TO_ATTACK_AGAIN) {
                    state = STATE_NORMAL;
                    stateTime = 0;
                }
            }
            body.setLinearVelocity(0, velocity.y);
            return;
        }
        if (isFacingLeft)
            velocity.x = -VELOCIDAD_WALK;
        else
            velocity.x = VELOCIDAD_WALK;
        body.setLinearVelocity(velocity);
        stateTime += delta;
    }

    public void getHurt(int damage) {
        if (state != STATE_DEAD) {
            vidas -= damage;
            if (vidas <= 0) {
                state = STATE_DEAD;
                stateTime = 0;
            } else {
                if (state != STATE_HURT) {
                    state = STATE_HURT;
                    stateTime = 0;
                }
            }
        }
    }

    public void die() {
        if (state != STATE_DEAD) {
            state = STATE_DEAD;
            stateTime = 0;
        }
    }

    /**
     * Regresa si si ataco
     *
     * @return
     */
    public boolean attack() {
        if (state == STATE_NORMAL) {
            state = STATE_ATTACK;
            attack = true;
            stateTime = 0;
            return true;
        }
        return false;
    }

    public void didAttackEnemy() {
        attack = false;
    }
}
