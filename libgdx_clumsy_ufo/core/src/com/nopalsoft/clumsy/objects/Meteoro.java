package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.clumsy.game.arcade.WorldGameArcade;

public abstract class Meteoro implements Poolable {

    public static float VELOCIDAD_X = -1f;

    public static int STATE_NORMAL = 0;
    public static int STATE_DESTROY = 1;
    public int state;

    public Vector2 position;
    public float stateTime;

    public float angleDeg;

    public Meteoro() {
        position = new Vector2();
    }

    public abstract void init(WorldGameArcade oWorld, float x, float y);

    public abstract void update(float delta, Body body);

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
}
