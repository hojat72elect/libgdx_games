package com.nopalsoft.invaders.game_objects;

import com.badlogic.gdx.math.Circle;

public class Missile extends DynamicGameObject {

    public static final float WIDTH = 0.4f;
    public static final float HEIGHT = 1.4f;

    public static final float RADIO_EXPLOSION = 7.5f;
    public final float VELOCIDAD = 30;
    public static final float TIEMPO_EXPLODE = 0.05f * 19;
    public final static int STATE_DISPARADO = 0;
    public final static int STATE_EXPLOTANDO = 1;

    public float stateTime;
    public int state;

    /**
     * X and Y are the position of the tip of the ship
     *
     * @param x The same as Bob.x
     * @param y The same as Bob.y
     */
    public Missile(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        // I also initialize the radius because the explosion is going to be cool.
        boundsCircle = new Circle(position.x, position.y, RADIO_EXPLOSION);
        state = STATE_DISPARADO;
        stateTime = 0;
        velocity.set(0, VELOCIDAD);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundsRectangle.x = position.x - WIDTH / 2;
        boundsRectangle.y = position.y - HEIGHT / 2;
        boundsCircle.x = position.x;
        boundsCircle.y = position.y;
        stateTime += deltaTime;
    }

    public void hitTarget() {
        velocity.set(0, 0);
        stateTime = 0;
        state = STATE_EXPLOTANDO;
    }

}
