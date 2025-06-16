package com.nopalsoft.invaders.game_objects;

public class AlienShip extends DynamicGameObject {

    public static final float RADIUS = 1.5f;

    public static final float DRAW_WIDTH = 3.5f;
    public static final float DRAW_HEIGHT = 3.5f;

    public static final int MOVE_SIDES = 0;
    public static final int MOVE_DOWN = 2;
    public static final int EXPLODING = 3;
    public static final float SPEED = 4f;
    public static final float SPEED_DOWN = -3.5f;

    public static final float HORIZONTAL_MOVEMENT_RANGE = 6.7f;
    public static final float VERTICAL_MOVEMENT_RANGE = 1.2f;
    public static final float EXPLOSION_DURATION = 0.05f * 19;

    public final int POINTS_PER_HIT = 10;

    public int remainingLives;
    public int score;
    public float stateTime;
    public int state;
    float movedDistance;
    float speedIncrease;

    public AlienShip(int vida, float speedIncrease, float x, float y) {
        super(x, y, RADIUS);
        stateTime = 0;
        state = MOVE_SIDES;
        velocity.set(SPEED, SPEED_DOWN);
        movedDistance = 0;
        score = POINTS_PER_HIT;
        remainingLives = vida;
        this.speedIncrease = 1 + speedIncrease;
    }

    public void update(float deltaTime) {
        if (state != EXPLODING) {
            switch (state) {
                case MOVE_SIDES:
                    position.x += velocity.x * deltaTime * speedIncrease;
                    movedDistance += Math.abs(velocity.x * deltaTime) * speedIncrease;
                    if (movedDistance > HORIZONTAL_MOVEMENT_RANGE) {
                        state = MOVE_DOWN;
                        velocity.x *= -1;
                        movedDistance = 0;
                    }
                    break;
                case MOVE_DOWN:
                    position.y += velocity.y * deltaTime * speedIncrease;
                    movedDistance += Math.abs(velocity.x * deltaTime) * speedIncrease;
                    if (movedDistance > VERTICAL_MOVEMENT_RANGE) {
                        state = MOVE_SIDES;
                        movedDistance = 0;
                    }
                    break;
            }
        }

        boundsCircle.x = position.x;
        boundsCircle.y = position.y;
        stateTime += deltaTime;
    }

    /**
     * Calling this method is bullet power 1
     */
    public void beingHit() {
        remainingLives--;
        if (remainingLives <= 0) {
            state = EXPLODING;
            velocity.add(0, 0);
            stateTime = 0;
        }
    }
}
