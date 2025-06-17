package com.nopalsoft.slamthebird.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.slamthebird.Settings;

import java.util.Random;

public class Enemy {
    public static float WIDTH = .4f;
    public static float HEIGHT = .4f;

    public static int STATE_JUST_APPEARED = 0;
    public static int STATE_FLYING = 1;
    public static int STATE_HIT = 2;
    public static int STATE_EVOLVING = 3;// So that it can fly again
    public static int STATE_DEAD = 4;

    public float TIME_JUST_APPEARED = 1.7f;

    public static float MAX_SPEED_BLUE = 1.75f;
    public static float MAX_SPEED_RED = 3.25f;

    public float TIME_TO_CHANGE_VELOCITY = 3;
    public float timeToChangeVelocity;

    public float TIME_TO_EVOLVE = 3f;
    public float timeToEvolve;

    public float EVOLVING_DURATION = 1.5f;

    public float FROZEN_DURATION = 5f;
    float durationFrozen;

    public Vector2 position;

    public Vector2 velocity;

    public boolean isFrozen;

    public int state;
    public float stateTime;

    public int lives;

    public float visualScale;

    public Enemy(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_JUST_APPEARED;
        lives = 2;
        stateTime = 0;
        velocity = new Vector2();
        isFrozen = false;
        durationFrozen = 0;
        FROZEN_DURATION += Settings.BOOST_FREEZE;
    }

    public void update(float delta, Body body, Random oRan) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        if (isFrozen) {
            body.setLinearVelocity(0, 0);
            if (durationFrozen >= FROZEN_DURATION) {
                isFrozen = false;
                durationFrozen = 0;
                setNewVelocity(body, oRan, false);
            }
            durationFrozen += delta;
            return;// Nothing else can be done if it's frozen. It doesn't move, it doesn't change speed, it doesn't evolve, it doesn't swim.
        }

        // Whatever happens, I don't want it to be higher than 10f.
        if (position.y > 10f) {
            velocity = body.getLinearVelocity();
            body.setLinearVelocity(velocity.x, velocity.y * -1);
        }
        if (state == STATE_JUST_APPEARED) {
            visualScale = stateTime * 1.5f / TIME_JUST_APPEARED;// 1.5f maximum scale

            if (stateTime >= TIME_JUST_APPEARED) {
                state = STATE_FLYING;
                stateTime = 0;
                setNewVelocity(body, oRan, false);
            }
        }

        if (state != STATE_JUST_APPEARED) {

            timeToChangeVelocity += delta;
            if (timeToChangeVelocity >= TIME_TO_CHANGE_VELOCITY) {
                timeToChangeVelocity -= TIME_TO_CHANGE_VELOCITY;

                Vector2 vel = body.getLinearVelocity();

                // Change in X
                if (oRan.nextBoolean())
                    vel.x *= -1;

                if (state == STATE_FLYING) {
                    if (oRan.nextBoolean())
                        vel.y *= -1;
                }
                body.setLinearVelocity(vel);
            }
        }

        if (state == STATE_HIT) {
            body.setGravityScale(1);
            timeToEvolve += delta;
            if (timeToEvolve >= TIME_TO_EVOLVE) {
                state = STATE_EVOLVING;
                stateTime = 0;
                timeToEvolve = 0;
            }
        }

        if (state == STATE_EVOLVING && stateTime >= EVOLVING_DURATION) {
            state = STATE_FLYING;
            body.setGravityScale(0);
            setNewVelocity(body, oRan, true);
            lives = 3;
            stateTime = 0;
        }

        velocity = body.getLinearVelocity();

        limitSpeed(body);
        velocity = body.getLinearVelocity();

        stateTime += delta;
    }

    /**
     * Limits speed because sometimes the resulting force of the collision drove the enemy crazy.
     */
    private void limitSpeed(Body body) {
        float currentSpeed = MAX_SPEED_BLUE;
        if (lives == 3)
            currentSpeed = MAX_SPEED_RED;

        if (velocity.x > currentSpeed) {
            velocity.x = currentSpeed;
        } else if (velocity.x < -currentSpeed) {
            velocity.x = -currentSpeed;
        }

        if (lives > 1) {// So the bird falls quickly if I take off its wings
            if (velocity.y > currentSpeed) {
                velocity.y = currentSpeed;
            } else if (velocity.y < -currentSpeed) {
                velocity.y = -currentSpeed;
            }
        }
        body.setLinearVelocity(velocity);
    }

    /**
     * If it is touching the floor I make the velocity in Y always generate positive.
     */
    private void setNewVelocity(Body body, Random random, boolean isTouchingFloor) {
        float currentSpeed = MAX_SPEED_BLUE;
        if (lives == 3)
            currentSpeed = MAX_SPEED_RED;

        float velocityX = random.nextFloat() * currentSpeed * 2 - currentSpeed;
        float velocityY;
        if (isTouchingFloor)
            velocityY = random.nextFloat() * currentSpeed;
        else
            velocityY = random.nextFloat() * currentSpeed * 2 - currentSpeed;

        body.setLinearVelocity(velocityX, velocityY);
    }

    public void hit() {
        lives--;
        if (lives == 1)
            state = STATE_HIT;
        else if (lives == 0)
            state = STATE_DEAD;

        stateTime = 0;
    }

    public void die() {
        lives = 0;
        state = STATE_DEAD;
        stateTime = 0;
    }

    public void setFrozen() {
        durationFrozen = 0;
        isFrozen = true;
    }
}
