package com.nopalsoft.sharkadventure.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.sharkadventure.screens.Screens;

public class Chain implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_REMOVE = 2;
	public int state;

	public final static float DRAW_WIDTH = .16f;
	public final static float DRAW_HEIGHT = .24f;

	public final static float WIDTH = .13f;
	public final static float HEIGHT = .21f;
	public static final float VELOCIDAD_X = -1;

	final public Vector2 position;
	public float angleDeg;

	public Chain() {
		position = new Vector2();

	}

	public void init(float x, float y) {
		position.set(x, y);
		state = STATE_NORMAL;

	}

	public void update(Body body, float delta) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		angleDeg = MathUtils.radDeg * body.getAngle();

		if (position.x < -3 || position.y > Screens.WORLD_HEIGHT + 3) {
			state = STATE_REMOVE;
		}
	}

	public void hit() {
		if (state == STATE_NORMAL) {
			state = STATE_REMOVE;
		}
	}

	@Override
	public void reset() {
	}

}
