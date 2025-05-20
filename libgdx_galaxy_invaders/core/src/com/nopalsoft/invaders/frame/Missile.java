package com.nopalsoft.invaders.frame;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
     * X y Y son la posicion de la punta de nave
     *
     * @param x La misma que Bob.x
     * @param y La misma que Bob.y
     */
    public Missile(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        // Tambien inicializo el radio porque la explosion va estar guapetona
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

    // Regresa la velocidad
    public Vector2 getVelocity() {
        Vector2 vel = new Vector2(velocity.x, velocity.y);
        return vel;
    }

}
