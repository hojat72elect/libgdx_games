package com.nopalsoft.ninjarunner.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;

public class Player {
    public final static int STATE_NORMAL = 0;// NORMAL APLICA PARA RUN,DASH,SLIDE,JUMP
    public final static int STATE_HURT = 1;
    public final static int STATE_DIZZY = 2;
    public final static int STATE_DEAD = 3;
    public final static int STATE_REVIVE = 4;
    public int state;

    public final static int TYPE_GIRL = 0;
    public final static int TYPE_BOY = 1;
    public final static int TYPE_NINJA = 2;
    public final int type;

    public final static float DRAW_WIDTH = 1.27f;
    public final static float DRAW_HEIGHT = 1.05f;

    public final static float WIDTH = .55f;
    public final static float HEIGHT = 1f;

    public final static float HEIGHT_SLIDE = .45f;

    public static final float RUN_SPEED = 3;
    public static final float DASH_SPEED = 7;

    public static float JUMP_SPEED = 5;
    public final float SECOND_JUMP_SPEED = 4;

    public final static float DURATION_DEAD = Assets.girlDeathAnimation.animationDuration + .5f;
    public final static float DURATION_HURT = Assets.girlHurtAnimation.animationDuration + .1f;
    public final static float DURATION_DIZZY = 1.25f;

    final float DURATION_MAGNET;
    float durationMagnet;

    final float DURATION_DASH = 5;
    float durationDash;

    final Vector2 initialPosition;
    public Vector2 position;
    public float stateTime;

    public boolean isJumping;// To know if I can draw the jumping animation

    public int numberOfFloorsInContact; // Floors you are currently touching if ==0 you cannot jump

    private boolean canJump;
    private boolean canDoubleJump;

    public boolean didGetHurtAtLeastOnce;

    public int lives;
    public final int MAX_LIVES = Settings.LEVEL_LIFE + 5;

    public boolean isDash;
    public boolean isSlide;
    public boolean isIdle;

    public boolean isMagnetEnabled = false;

    public Player(float x, float y, int type) {
        position = new Vector2(x, y);
        initialPosition = new Vector2(x, y);
        state = STATE_NORMAL;
        stateTime = 0;
        this.type = type;
        canJump = true;
        canDoubleJump = true;
        didGetHurtAtLeastOnce = false;
        isIdle = true;

        lives = MAX_LIVES;
        DURATION_MAGNET = 10;
    }

    public void update(float delta, Body body, boolean didJump, boolean isJumpPressed, boolean dash, boolean didSlide) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        isIdle = false;

        // It doesn't matter if he's alive/dizzy/ or whatever, time is running out.
        if (isMagnetEnabled) {
            durationMagnet += delta;
            if (durationMagnet >= DURATION_MAGNET) {
                durationMagnet = 0;
                isMagnetEnabled = false;
            }
        }

        if (state == STATE_REVIVE) {
            state = STATE_NORMAL;
            canJump = true;
            isJumping = false;
            canDoubleJump = true;
            stateTime = 0;
            lives = MAX_LIVES;
            initialPosition.y = 3;
            position.x = initialPosition.x;
            position.y = initialPosition.y;
            body.setTransform(initialPosition, 0);
            body.setLinearVelocity(0, 0);
        } else if (state == STATE_HURT) {
            stateTime += delta;
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
        } else if (state == STATE_DIZZY) {
            stateTime += delta;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            if (stateTime >= DURATION_DIZZY) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
            return;
        } else if (state == STATE_DEAD) {
            stateTime += delta;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            return;
        }

        Vector2 velocity = body.getLinearVelocity();

        if (didJump && (canJump || canDoubleJump)) {
            velocity.y = JUMP_SPEED;

            if (!canJump) {
                canDoubleJump = false;
                velocity.y = SECOND_JUMP_SPEED;
            }

            canJump = false;
            isJumping = true;
            stateTime = 0;

            isSlide = false;

            body.setGravityScale(.9f);
            Assets.playSound(Assets.jumpSound, 1);
        }
        if (!isJumpPressed)
            body.setGravityScale(1);

        if (!isJumping) {
            isSlide = didSlide;
        }

        if (dash) {
            isDash = true;
            durationDash = 0;
        }

        if (isDash) {
            durationDash += delta;
            velocity.x = DASH_SPEED;
            if (durationDash >= DURATION_DASH) {
                isDash = false;
                stateTime = 0;
                velocity.x = RUN_SPEED;
            }
        } else {
            velocity.x = RUN_SPEED;
        }
        stateTime += delta;

        body.setLinearVelocity(velocity);
    }

    public void getHurt() {
        if (state != STATE_NORMAL)
            return;

        lives--;
        if (lives > 0) {
            state = STATE_HURT;
        } else {
            state = STATE_DEAD;
        }
        stateTime = 0;
        didGetHurtAtLeastOnce = true;
    }

    public void getDizzy() {
        if (state != STATE_NORMAL)
            return;

        lives--;
        if (lives > 0) {
            state = STATE_DIZZY;
        } else {
            state = STATE_DEAD;
        }
        stateTime = 0;
        didGetHurtAtLeastOnce = true;
    }

    public void die() {
        if (state != STATE_DEAD) {
            lives = 0;

            state = STATE_DEAD;
            stateTime = 0;
        }
    }

    public void touchFloor() {
        numberOfFloorsInContact++;

        canJump = true;
        isJumping = false;
        canDoubleJump = true;
        if (state == STATE_NORMAL)
            stateTime = 0;
    }

    public void endTouchFloor() {
        numberOfFloorsInContact--;
        if (numberOfFloorsInContact == 0) {
            canJump = false;

            // Si dejo de tocar el piso porque salto todavia puede saltar otra vez
            if (!isJumping)
                canDoubleJump = false;
        }
    }

    public void updateStateTime(float delta) {
        stateTime += delta;
    }

    public void setPickUpMagnet() {
        durationMagnet = 0;
        isMagnetEnabled = true;
    }
}
