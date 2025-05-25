package com.nopalsoft.sharkadventure.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Items implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_REMOVE = 1;
	public int state;

	public final static int TIPO_CORAZON = 0;
	public final static int TIPO_CARNE = 1;
	public int tipo;

	public final static float DRAW_WIDTH = .45f;
	public final static float DRAW_HEIGHT = .45f;

	public final static float WIDTH = .35f;
	public final static float HEIGHT = .35f;
	public static final float VELOCIDAD_X = -1f;

	final public Vector2 position;

	public Items() {
		position = new Vector2();
	}

	public void init(float x, float y) {
		position.set(x, y);
		state = STATE_NORMAL;
		tipo = MathUtils.random(1);

	}

	public void update(Body body, float delta) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (position.x < -3) {
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
