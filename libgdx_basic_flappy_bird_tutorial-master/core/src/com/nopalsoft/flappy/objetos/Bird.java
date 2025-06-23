package com.nopalsoft.flappy.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bird {
    public static float JUMP_SPEED = 5;

    public static int STATE_NORMAL = 0;
    public static int STATE_DEAD = 1;
    public int state;

    public Vector2 position;
    public float stateTime;

    public Bird(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_NORMAL;
    }

    // Update object position to match with the Box2D body.
    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        stateTime += delta;
    }

    // Called when the bird crash with a pipe
    public void hurt() {
        state = STATE_DEAD;
        stateTime = 0;
    }

}
