package com.nopalsoft.invaders.frame;

import com.badlogic.gdx.Gdx;

import com.nopalsoft.invaders.game.World;

public class Nave extends DynamicGameObject {

    public static final float DRAW_WIDTH = 4.5f;
    public static final float DRAW_HEIGHT = 3.6f;

    public static final float WIDTH = 4f;
    public static final float HEIGHT = 2.5f;

    public static final float NAVE_MOVE_SPEED = 50;

    public static final int NAVE_STATE_NORMAL = 0;
    public static final int NAVE_STATE_EXPLODE = 1;
    public static final int NAVE_STATE_BEING_HIT = 2;

    public static final float TIEMPO_EXPLODE = 0.05f * 19;
    public static final float TIEMPO_BEING_HIT = 0.05f * 21; // uno mas pa que tenga tantillo tiempo de pensar jaja

    public int vidasEscudo;
    public int vidas;
    public int state;
    public float stateTime;

    public Nave(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        vidas = 3;
        vidasEscudo = 1;// empizas con 1 de shield por si los putos te pegan
        state = NAVE_STATE_NORMAL;
        Gdx.app.log("Estado", "Se creo la nave");
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundsRectangle.x = position.x - boundsRectangle.width / 2;
        boundsRectangle.y = position.y - boundsRectangle.height / 2;

        if (state == NAVE_STATE_BEING_HIT && stateTime > TIEMPO_BEING_HIT) {
            state = NAVE_STATE_NORMAL;
            stateTime = 0;
            Gdx.app.log("Estado", "Se cambio a normal");
        }

        if (position.x < WIDTH / 2)
            position.x = WIDTH / 2;
        if (position.x > World.WIDTH - WIDTH / 2)
            position.x = World.WIDTH - WIDTH / 2;
        stateTime += deltaTime;
    }

    public void beingHit() {
        if (vidasEscudo > 0) {
            vidasEscudo--;
        } else {
            vidas--;
            if (vidas <= 0) {
                state = NAVE_STATE_EXPLODE;
                stateTime = 0;
                velocity.set(0, 0);
            } else {
                state = NAVE_STATE_BEING_HIT;
                stateTime = 0;
            }
        }
    }

    public void hitVidaExtra() {
        if (vidas < 99) {
            vidas++;
        }
    }

    public void hitEscudo() {
        stateTime = 0;
        vidasEscudo = 3;
    }

}
