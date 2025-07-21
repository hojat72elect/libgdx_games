package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.clumsy.game.arcade.WorldGameArcade;

public abstract class Asteroid0 implements Poolable {

    public static int STATE_NORMAL = 0;
    public static int STATE_DESTROY = 1;
    public int state;

    public Vector2 position;
    public float stateTime;

    public float angleDeg;

    public Asteroid0() {
        position = new Vector2();
    }

    public abstract void init(WorldGameArcade worldGameArcade, float x, float y);

    public abstract void update(float delta, Body body);

    @Override
    public void reset() {
    }
}
