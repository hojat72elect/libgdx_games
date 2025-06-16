package com.nopalsoft.invaders.game_objects;

public class Boost extends DynamicGameObject {

    public static final float DRAW_SIZE = 5;
    public static final float RADIUS = 1f;
    public static final float SPEED = -10f;

    public static final int EXTRA_LIFE_BOOST = 0;
    public static final int EXTRA_MISSILE_BOOST = 1;
    public static final int EXTRA_SHIELD_BOOST = 2;
    public static final int SHIELD = 3;
    public final int type;
    int stateTime;

    public Boost(int type, float x, float y) {
        super(x, y, RADIUS);
        this.type = type;
        velocity.add(0, SPEED);
        stateTime = 0;
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundsCircle.x = position.x;
        boundsCircle.y = position.y;
        stateTime += deltaTime;
    }
}
