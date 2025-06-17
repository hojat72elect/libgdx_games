package com.nopalsoft.slamthebird.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Coin implements Poolable {

    public static final float RADIUS = .15f;

    public static float MOVE_SPEED = 1;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_TAKEN = 1;
    public int state;

    public Vector2 position;
    public float stateTime;

    public Coin() {
        position = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        stateTime = 0;
        state = STATE_NORMAL;
    }

    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        stateTime += delta;
    }

    @Override
    public void reset() {

    }

    public static Body createCoinBody(World world, float x, float y, float velocityX) {
        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDefinition);

        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 1;
        fixtureDefinition.restitution = .5f;
        fixtureDefinition.friction = 0;
        fixtureDefinition.filter.groupIndex = -1;

        body.setGravityScale(0);
        body.createFixture(fixtureDefinition);
        body.setLinearVelocity(velocityX, 0);

        return body;
    }
}
