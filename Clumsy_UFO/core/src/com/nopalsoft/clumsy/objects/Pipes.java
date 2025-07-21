package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Pipes {
    public static int UPPER_PIPE = 0;
    public static int LOWER_PIPE = 1;

    public static float WIDTH = .7f;
    public static float HEIGHT = 4f;

    public static int STATE_NORMAL = 0;
    public static int STATE_DESTROY = 1;

    public static float SPEED_X = -2f;

    public Vector2 position;
    public float stateTime;

    public int state;
    public int type;

    public Pipes(float x, float y, int type) {
        position = new Vector2(x, y);
        stateTime = 0;
        state = STATE_NORMAL;
        this.type = type;
    }

    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        stateTime += delta;
    }
}
