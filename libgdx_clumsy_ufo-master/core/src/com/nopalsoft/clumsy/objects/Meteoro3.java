package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nopalsoft.clumsy.game.arcade.WorldGameArcade;

public class Meteoro3 extends Meteoro {

	float VEL_ROTACION = 50;
	float VEL_X = -2f;

	@Override
	public void init(WorldGameArcade oWorld, float x, float y) {
		position.set(x, y);
		stateTime = 0;
		state = STATE_NORMAL;

		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.KinematicBody;

		Body oBody = oWorld.oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(.08f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 8;
		fixture.restitution = 0;
		fixture.friction = 0;
		oBody.createFixture(fixture);

		oBody.setUserData(this);
		oBody.setLinearVelocity(VEL_X, 0);
		oBody.setAngularVelocity((float) Math.toRadians(VEL_ROTACION));

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
