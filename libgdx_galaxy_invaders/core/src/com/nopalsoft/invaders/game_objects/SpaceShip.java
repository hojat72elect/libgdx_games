package com.nopalsoft.invaders.game_objects;

import com.badlogic.gdx.Gdx;
import com.nopalsoft.invaders.game.World;

public class SpaceShip extends DynamicGameObject {

    public static final float DRAW_WIDTH = 4.5f;
    public static final float DRAW_HEIGHT = 3.6f;

    public static final float WIDTH = 4f;
    public static final float HEIGHT = 2.5f;

    public static final float SPACESHIP_SPEED = 50;

    public static final int SPACESHIP_STATE_NORMAL = 0;
    public static final int SPACESHIP_STATE_EXPLODE = 1;
    public static final int SPACESHIP_STATE_BEING_HIT = 2;

    public static final float EXPLOSION_DURATION = 0.05f * 19;
    public static final float BEING_HIT_DURATION = 0.05f * 21; // One more so that I have a little time to think haha

    public int shieldCount;
    public int lives;
    public int state;
    public float stateTime;

    public SpaceShip(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        lives = 3;
        shieldCount = 1;// You start with 1 shield in case the bastards hit you.
        state = SPACESHIP_STATE_NORMAL;
        Gdx.app.log("State", "The ship was created");
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundsRectangle.x = position.x - boundsRectangle.width / 2;
        boundsRectangle.y = position.y - boundsRectangle.height / 2;

        if (state == SPACESHIP_STATE_BEING_HIT && stateTime > BEING_HIT_DURATION) {
            state = SPACESHIP_STATE_NORMAL;
            stateTime = 0;
            Gdx.app.log("State", "It changed to normal");
        }

        if (position.x < WIDTH / 2)
            position.x = WIDTH / 2;
        if (position.x > World.WIDTH - WIDTH / 2)
            position.x = World.WIDTH - WIDTH / 2;
        stateTime += deltaTime;
    }

    public void beingHit() {
        if (shieldCount > 0) {
            shieldCount--;
        } else {
            lives--;
            if (lives <= 0) {
                state = SPACESHIP_STATE_EXPLODE;
                stateTime = 0;
                velocity.set(0, 0);
            } else {
                state = SPACESHIP_STATE_BEING_HIT;
                stateTime = 0;
            }
        }
    }

    public void hitVidaExtra() {
        if (lives < 99) {
            lives++;
        }
    }

    public void hitEscudo() {
        stateTime = 0;
        shieldCount = 3;
    }
}
