package com.nopalsoft.zombiekiller.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiekiller.Assets;

public class Zombie {

    public final static int STATE_RISE = 0;
    public final static int STATE_NORMAL = 1;
    public final static int STATE_HURT = 2;
    public final static int STATE_DEAD = 3;

    public final static int TYPE_KID = 0;
    public final static int TYPE_FRANK = 1;
    public final static int TYPE_CUASY = 2;
    public final static int TYPE_PAN = 3;
    public final static int TYPE_MUMMY = 4;

    public final static float RISE_DURATION = Assets.zombieKidRise.animationDuration + .2f;
    public final static float DEAD_DURATION = Assets.zombieKidDie.animationDuration + .2f;
    public final static float HURT_DURATION = .3f;

    public final int type;
    public final int MAX_LIFE;
    public final float TIME_TO_HURT_PLAYER = 1;
    public int state;

    public float WALK_SPEED;
    public float FORCE_IMPACT;

    public Vector2 position;
    public float stateTime;
    public boolean isFacingLeft;
    public boolean isWalking;
    public boolean canUpdate;
    public boolean isFollowing;
    public int lives;
    public boolean isTouchingPlayer;
    float timeToHurtPlayer;

    public Zombie(float x, float y, int type) {
        position = new Vector2(x, y);
        state = STATE_RISE;
        stateTime = 0;
        this.type = type;
        canUpdate = false;

        isFollowing = true;

        switch (type) {
            case TYPE_KID:
                lives = 5;
                FORCE_IMPACT = 2.5f;
                WALK_SPEED = 1.1f;
                break;

            case TYPE_CUASY:
                lives = 15;
                FORCE_IMPACT = 3;
                WALK_SPEED = .5f;
                break;

            case TYPE_MUMMY:
                lives = 100;
                FORCE_IMPACT = 8;
                WALK_SPEED = .5f;
                break;

            case TYPE_PAN:
                lives = 50;
                FORCE_IMPACT = 4;
                WALK_SPEED = .7f;
                break;

            case TYPE_FRANK:
                lives = 120;
                FORCE_IMPACT = 5;
                WALK_SPEED = 1.3f;
                break;
        }
        MAX_LIFE = lives;
    }

    public void update(float delta, Body body, float accelX, Hero oHero) {
        body.setAwake(true);
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        Vector2 velocity = body.getLinearVelocity();

        // So that if they cannot be updated, at least the objects fall and remain at ground level. Then the earth looks stuck to the ground when state==Rise
        if (!canUpdate) {
            body.setLinearVelocity(0, velocity.y);
            return;
        }

        isFacingLeft = !(oHero.position.x > position.x);

        if (state == STATE_RISE) {
            stateTime += delta;
            if (stateTime >= RISE_DURATION) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
            return;
        } else if (state == STATE_DEAD) {
            stateTime += delta;
            return;
        } else if (state == STATE_HURT) {
            stateTime += delta;
            if (stateTime >= HURT_DURATION) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
            return;
        }

        if (isTouchingPlayer) {
            timeToHurtPlayer += delta;
            if (timeToHurtPlayer >= TIME_TO_HURT_PLAYER) {
                timeToHurtPlayer -= TIME_TO_HURT_PLAYER;
                oHero.getHurt();
            }
        } else {
            timeToHurtPlayer = 0;
        }

        if (isFollowing) {
            if (oHero.position.x + .1f < position.x)
                accelX = -1;
            else if (oHero.position.x - .1f > position.x)
                accelX = 1;
            else
                accelX = 0;
        }

        if (accelX == -1) {
            velocity.x = -WALK_SPEED;
            isFacingLeft = true;
            isWalking = true;
        } else if (accelX == 1) {
            velocity.x = WALK_SPEED;
            isFacingLeft = false;
            isWalking = true;
        } else {
            velocity.x = 0;
            isWalking = false;
        }

        body.setLinearVelocity(velocity);

        stateTime += delta;
    }

    public void getHurt(int damage) {
        if (state == STATE_NORMAL || state == STATE_HURT) {
            lives -= damage;
            if (lives <= 0) {
                state = STATE_DEAD;
                stateTime = 0;
            } else {
                if (state == STATE_NORMAL) {
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
}
