package com.nopalsoft.superjumper.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.superjumper.screens.Screens;

public class Enemigo implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DEAD = 1;
	public int state;

	public final static float DRAW_WIDTH = .95f;
	public final static float DRAW_HEIGHT = .6f;

	public final static float WIDTH = .65f;
	public final static float HEIGHT = .4f;

	public final static float VELOCIDAD_X = 2;

	final public Vector2 position;
	public Vector2 velocidad;
	public float angleDeg;

	public float stateTime;

	public Enemigo() {
		position = new Vector2();
		velocidad = new Vector2();

	}

	public void init(float x, float y) {
		position.set(x, y);
		velocidad.set(0, 0);// La velocidad se la pongo desde el metodo donde la creo
		stateTime = 0;
		state = STATE_NORMAL;
		angleDeg = 0;

	}

	public void update(Body body, float delta) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		velocidad = body.getLinearVelocity();

		if (state == STATE_NORMAL) {

			if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
				velocidad.x = velocidad.x * -1;
			}

		}
		else {
			body.setAngularVelocity(MathUtils.degRad * 360);
			if (velocidad.y < -5)
				velocidad.y = -5;
		}

		body.setLinearVelocity(velocidad);

		angleDeg = body.getAngle() * MathUtils.radDeg;

		velocidad = body.getLinearVelocity();
		stateTime += delta;

	}

	public void hit() {
		if (state == STATE_NORMAL) {
			state = STATE_DEAD;
			stateTime = 0;
		}

	}

	@Override
	public void reset() {
	}

}
