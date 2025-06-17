package com.nopalsoft.slamthebird.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;

public class Robot {
    public static float RADIUS = .28f;

    public static int STATE_FALLING = 0;
    public static int STATE_JUMPING = 1;
    public static int STATE_DEAD = 2;

    public float JUMP_SPEED = 6.25f;
    public float MOVE_SPEED = 5f;

    public float DURATION_SUPER_JUMP = 5;
    public float durationSuperJump;

    public float INVINCIBLE_DURATION = 5;
    public float invincibilityDuration;

    public static final float DEAD_ANIMATION_DURATION = 2;

    public Vector2 position;

    public int state;
    public float stateTime;

    public boolean jump, slam;
    public boolean isSuperJump;
    public boolean isInvincible;

    public float angleDegrees;
    public Vector2 velocity;

    public Robot(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_JUMPING;
        velocity = new Vector2();
        jump = true;// to make the first jump

        DURATION_SUPER_JUMP += Settings.BOOST_SUPER_JUMP;
        INVINCIBLE_DURATION += Settings.BOOST_INVINCIBLE;
    }

    public void update(float delta, Body body, float acelX, boolean slam) {
        this.slam = slam;// To draw the rapid fall =)
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        angleDegrees = 0;
        if (state == STATE_FALLING || state == STATE_JUMPING) {

            if (slam)
                body.setGravityScale(2.5f);
            else
                body.setGravityScale(1);

            if (jump) {
                jump = false;
                state = STATE_JUMPING;
                stateTime = 0;
                if (isSuperJump) {
                    body.setLinearVelocity(body.getLinearVelocity().x,
                            JUMP_SPEED + 3);
                } else {
                    body.setLinearVelocity(body.getLinearVelocity().x,
                            JUMP_SPEED);
                }
            }

            Vector2 velocity = body.getLinearVelocity();

            if (velocity.y < 0 && state != STATE_FALLING) {
                state = STATE_FALLING;
                stateTime = 0;
            }
            body.setLinearVelocity(acelX * MOVE_SPEED, velocity.y);

            if (isSuperJump) {
                durationSuperJump += delta;
                if (durationSuperJump >= DURATION_SUPER_JUMP) {
                    isSuperJump = false;
                    durationSuperJump = 0;
                }
            }

            if (isInvincible) {
                invincibilityDuration += delta;
                if (invincibilityDuration >= INVINCIBLE_DURATION) {
                    isInvincible = false;
                    invincibilityDuration = 0;
                }
            }
        } else if (state == STATE_DEAD) {
            body.setLinearVelocity(0, -3);
            body.setFixedRotation(false);
            angleDegrees = (float) Math.toDegrees(body.getAngle());
            body.setAngularVelocity((float) Math.toRadians(20));
        }
        velocity = body.getLinearVelocity();
        stateTime += delta;
    }

    public void updateReady(Body body, float acelX) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        body.setLinearVelocity(acelX * MOVE_SPEED, 0);
        velocity = body.getLinearVelocity();
    }

    public void jump() {
        if (state == STATE_FALLING) {
            jump = true;
            stateTime = 0;
            Assets.playSound(Assets.soundJump);
        }
    }

    /**
     * The robot is hit and dies.
     */
    public void hit() {
        state = STATE_DEAD;
        stateTime = 0;
    }
}
