package com.nopalsoft.sharkadventure.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.sharkadventure.screens.Screens;

public class Blast implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_HIT = 1;
	public final static int STATE_REMOVE = 2;
	public int state;

	public final static float DURATION_HIT = .05f * 6f;

	public final static float VELOCIDAD_X = 5.5f;
	public final static float DRAW_WIDTH = .32f;
	public final static float DRAW_HEIGHT = .32f;

	public final static float WIDTH = .31f;
	public final static float HEIGHT = .31f;

	final public Vector2 position;
	public Vector2 velocity;
	public float stateTime;

	public Blast() {
		position = new Vector2();
		velocity = new Vector2();

	}

	public void init(float x, float y) {
		position.set(x, y);
		velocity.set(0, 0);
		stateTime = 0;
		state = STATE_NORMAL;
	}

	public void update(Body body, float delta) {
		if (state == STATE_NORMAL) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;

			velocity = body.getLinearVelocity();

			if (position.y < -3 || position.x > Screens.WORLD_WIDTH + 3)
				hit();
		}
		else if (state == STATE_HIT && stateTime >= DURATION_HIT) {
			state = STATE_REMOVE;
			stateTime = 0;
		}

		stateTime += delta;

	}

	public void hit() {
		if (state == STATE_NORMAL) {
			state = STATE_HIT;
			stateTime = 0;
		}
	}

	@Override
	public void reset() {
	}

}
