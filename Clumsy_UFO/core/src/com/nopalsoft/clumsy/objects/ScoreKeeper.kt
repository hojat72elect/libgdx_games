package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class ScoreKeeper {
    public static float WIDTH = .1f;
    public static float HEIGHT = 1.65f;

    public static int STATE_NORMAL = 0;
    public static int STATE_DESTROY = 1;

    public static float SPEED_X = Pipes.SPEED_X;

    public Vector2 position;
    public float stateTime;

    public int state;

    public ScoreKeeper() {
        position = new Vector2();
        stateTime = 0;
        state = STATE_NORMAL;
    }

    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        stateTime += delta;
    }
}
