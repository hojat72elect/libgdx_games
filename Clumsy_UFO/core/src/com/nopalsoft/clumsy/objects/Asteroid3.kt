package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nopalsoft.clumsy.game.arcade.WorldGameArcade;

public class Asteroid3 extends Asteroid0 {

    float ROTATION_SPEED = 50;
    float X_SPEED = -2f;

    @Override
    public void init(WorldGameArcade worldGameArcade, float x, float y) {
        position.set(x, y);
        stateTime = 0;
        state = STATE_NORMAL;

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.KinematicBody;

        Body body = worldGameArcade.oWorldBox.createBody(bodyDefinition);

        CircleShape shape = new CircleShape();
        shape.setRadius(.08f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 8;
        fixtureDefinition.restitution = 0;
        fixtureDefinition.friction = 0;
        body.createFixture(fixtureDefinition);

        body.setUserData(this);
        body.setLinearVelocity(X_SPEED, 0);
        body.setAngularVelocity((float) Math.toRadians(ROTATION_SPEED));

        shape.dispose();
    }

    @Override
    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        angleDeg = (float) Math.toDegrees(body.getAngle());
        stateTime += delta;
    }
}
