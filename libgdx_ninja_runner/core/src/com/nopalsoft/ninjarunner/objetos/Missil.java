package com.nopalsoft.ninjarunner.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.ninjarunner.Assets;


public class Missil implements Poolable, Comparable<Missil> {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_EXPLODE = 1;
	public final static int STATE_DESTROY = 2;
	public int state;

	private final static float DURATION_EXPLOSION = Assets.explosion.animationDuration + .1f;

	public static final float WIDTH = 1.27f;
	public static final float HEIGHT = .44f;

	public static final float VELOCIDAD_X = -2.5f;

	public final Vector2 position;
	public float stateTime;
	public float distanceFromPersonaje;

	public Missil() {
		position = new Vector2();
	}

	public void init(float x, float y) {
		position.set(x, y);
		state = STATE_NORMAL;
		stateTime = 0;

	}

	public void update(float delta, Body body, Personaje oPersonaje) {
		if (state == STATE_NORMAL) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;
			
			
		}
		if (state == STATE_EXPLODE) {

			if (stateTime >= DURATION_EXPLOSION) {
				state = STATE_DESTROY;
				stateTime = 0;
			}
		}

		distanceFromPersonaje = oPersonaje.position.dst(position);
		stateTime += delta;
	}

	public void setHitTarget() {
		if (state == STATE_NORMAL) {
			state = STATE_EXPLODE;
			stateTime = 0;
		}
	}

	public void setDestroy() {
		if (state != STATE_DESTROY) {
			state = STATE_DESTROY;
			stateTime = 0;
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public int compareTo(Missil o2) {
		if (distanceFromPersonaje > o2.distanceFromPersonaje)
			return 1;
		else if (distanceFromPersonaje < o2.distanceFromPersonaje)
			return -1;
		else
			return 0;
	}

}
