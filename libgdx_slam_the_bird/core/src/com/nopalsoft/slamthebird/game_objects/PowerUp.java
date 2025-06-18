package com.nopalsoft.slamthebird.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.slamthebird.game.WorldGame;

public class PowerUp implements Poolable {

    public static final int TYPE_SUPER_JUMP = 0;
    public static final int TYPE_INVINCIBLE = 1;
    public static final int TYPE_COIN_RAIN = 2;
    public static final int TYPE_FREEZE = 3;

    public static float DURATION_AVAILABLE = 5;

    public static int STATE_NORMAL = 0;
    public static int STATE_TAKEN = 1;
    public int state;

    public Vector2 position;
    public float stateTime;
    public int type;

    public PowerUp() {
        position = new Vector2();
    }

    public void init(WorldGame oWorld, float x, float y, int type) {
        this.type = type;
        position.set(x, y);
        stateTime = 0;
        state = STATE_NORMAL;

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.KinematicBody;

        Body body = oWorld.world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.15f, .15f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 8;
        fixtureDefinition.restitution = 0;
        fixtureDefinition.friction = 0;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);

        body.setUserData(this);
        shape.dispose();
    }

    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        stateTime += delta;

        if (stateTime >= DURATION_AVAILABLE) {
            state = STATE_TAKEN;
            stateTime = 0;
        }
    }

    public void hit() {
        state = STATE_TAKEN;
        stateTime = 0;
    }

    @Override
    public void reset() {
    }
}
