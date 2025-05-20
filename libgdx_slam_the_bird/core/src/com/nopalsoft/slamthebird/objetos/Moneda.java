package com.nopalsoft.slamthebird.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Moneda implements Poolable {

	public static final float RADIUS = .15f;

	public static float VELOCIDAD_MOVE = 1;

	public static final int STATE_NORMAL = 0;
	public static final int STATE_TAKEN = 1;
	public int state;

	public Vector2 position;
	public float stateTime;

	public Moneda() {
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

	public static Body crearMoneda(World oWorldBox, float x, float y, float velocidadX) {
		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1;
		fixture.restitution = .5f;
		fixture.friction = 0;
		fixture.filter.groupIndex = -1;

		oBody.setGravityScale(0);
		oBody.createFixture(fixture);
		oBody.setLinearVelocity(velocidadX, 0);

		return oBody;

	}
}
