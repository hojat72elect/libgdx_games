package com.nopalsoft.ninjarunner.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Mascota {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	public static enum Tipo {
		GALLINA_ROSA, BOMBA

	}

	public final Tipo tipo;

	public final static float VELOCIDAD = 5f;

	public static final float RADIUS = .25f;

	public final float drawWidth;
	public final float drawHeight;

	public final float dashDrawWidth;
	public final float dashDrawHeight;

	public final Vector2 position;
	final public Vector2 targetPosition;
	public Vector2 velocity;
	public float stateTime;

	public Mascota(float x, float y, Tipo tipo) {
		this.tipo = tipo;
		position = new Vector2(x, y);
		targetPosition = new Vector2(x, y);
		velocity = new Vector2();
		state = STATE_NORMAL;
		stateTime = 0;

		switch (tipo) {
		case GALLINA_ROSA:
			drawWidth = .73f;
			drawHeight = .66f;
			dashDrawWidth = 2.36f;
			dashDrawHeight = 1.25f;
			break;
		default:
		case BOMBA:
			drawWidth = dashDrawWidth = .52f;
			drawHeight = dashDrawHeight = .64f;
			break;

		}

	}

	public void update(Body body, float delta, float targetX, float targetY) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		targetPosition.set(targetX, targetY);

		velocity = body.getLinearVelocity();
		velocity.set(targetPosition).sub(position).scl(VELOCIDAD);
		body.setLinearVelocity(velocity);
		stateTime += delta;
	}

	public void setDestroy() {
		if (state == STATE_NORMAL) {
			state = STATE_DESTROY;
			stateTime = 0;
		}
	}

	public void updateStateTime(float delta) {
		stateTime += delta;

	}

}
