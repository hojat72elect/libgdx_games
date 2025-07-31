package com.nopalsoft.zombiekiller.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;

public class Hero {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_HURT = 1;
    public final static int STATE_DEAD = 2;

    public final static int TYPE_FORCE = 0;
    public final static int TYPE_RAMBO = 1;
    public final static int TYPE_SOLDIER = 2;
    public final static int TYPE_SWAT = 3;
    public final static int TYPE_VADER = 4;

    public final static float DURATION_DEAD = Assets.heroForceDie.animationDuration + .2f;
    public final static float DURATION_HURT = .5f;
    public final static float DURATION_IS_FIRING = Assets.heroForceShoot.animationDuration + .1f;

    public static float JUMP_SPEED = 5;
    public static float WALK_SPEED = 1.5f;

    public final int type;

    public final int MAX_LIVES = Settings.LEVEL_LIFE + 3;
    public final int MAX_SHIELDS = Settings.LEVEL_SHIELD + 1;

    public int state;
    public Vector2 position;
    public float stateTime;
    public boolean isFacingLeft;
    public boolean isWalking;
    public boolean isFiring;
    public boolean isClimbing;
    public boolean canJump;
    public Body bodyCrate;// Crate body standing
    public boolean isOnStairs; // True if you touch the stairs
    public int lives;
    public int shield;

    public Hero(float x, float y, int tipo) {
        position = new Vector2(x, y);
        state = STATE_NORMAL;
        stateTime = 0;
        this.type = tipo;
        canJump = true;

        shield = MAX_SHIELDS;
        lives = MAX_LIVES;
    }

    public void update(float delta, Body body, boolean didJump, float accelX, float accelY) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        if (state == STATE_HURT) {
            stateTime += delta;
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
            return;
        } else if (state == STATE_DEAD) {
            stateTime += delta;
            return;
        }

        if (isFiring && stateTime >= DURATION_IS_FIRING) {
            isFiring = false;
            stateTime = 0;
        }

        Vector2 velocity = body.getLinearVelocity();

        if (didJump && canJump) {
            velocity.y = JUMP_SPEED;
            canJump = false;

            Assets.playSound(Assets.jump, 1);
        }

        if (isOnStairs) {
            if (accelY != 0) {
                isClimbing = true;
                body.setGravityScale(0);
            }

            if (accelY == 1) {
                velocity.y = 1;
            } else if (accelY == -1) {
                velocity.y = -1;
            }
            // If he hasn't started climbing, I don't limit the speed because if he jumps and I'm only on the stairs, he'll fall at speed 0.
            else if (isClimbing) {
                velocity.y = 0;
            }
        } else {
            body.setGravityScale(1);
            isClimbing = false;
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
            if (bodyCrate != null)
                velocity.x = bodyCrate.getLinearVelocity().x;
            else
                velocity.x = 0;
            isWalking = false;
        }

        body.setLinearVelocity(velocity);

        if (isClimbing && accelY != 0)
            stateTime += delta;

        else if (!isClimbing)
            stateTime += delta;
    }

    public void getHurt() {
        if (state != STATE_NORMAL)
            return;

        if (shield > 0) {
            shield--;
            state = STATE_HURT;
            stateTime = 0;
            return;
        }

        lives--;
        if (lives > 0) {
            state = STATE_HURT;
        } else {
            state = STATE_DEAD;
        }
        stateTime = 0;
    }

    public void getShield() {
        shield += 2;
        if (shield > MAX_SHIELDS)
            shield = MAX_SHIELDS;
    }

    public void getHeart() {
        lives += 1;
        if (lives > MAX_LIVES)
            lives = MAX_LIVES;
    }

    public void die() {
        if (state != STATE_DEAD) {
            lives = 0;
            shield = 0;
            state = STATE_DEAD;
            stateTime = 0;
        }
    }

    public void fire() {
        isFiring = true;
        stateTime = 0;
    }
}
