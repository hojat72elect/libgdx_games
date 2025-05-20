package com.nopalsoft.superjumper.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Rayo implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	public final static float DRAW_WIDTH = .25f;
	public final static float DRAW_HEIGHT = .98f;

	public final static float WIDTH = .21f;
	public final static float HEIGHT = .93f;

	public final static float VELOCIDAD_Y = -3f;

	final public Vector2 position;

	public float stateTime;

	public Rayo() {
		position = new Vector2();
	}

	public void init(float x, float y) {
		position.set(x, y);
		stateTime = 0;
		state = STATE_NORMAL;

	}

	public void update(Body body, float delta) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		stateTime += delta;

	}

	public void destroy() {
		if (state == STATE_NORMAL) {
			state = STATE_DESTROY;
			stateTime = 0;
		}

	}

	@Override
	public void reset() {
	}

}
