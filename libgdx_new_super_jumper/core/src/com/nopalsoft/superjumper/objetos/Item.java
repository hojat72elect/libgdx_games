package com.nopalsoft.superjumper.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Item implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_TAKEN = 1;
	public int state;

	public final static float DRAW_WIDTH = .27f;
	public final static float DRAW_HEIGHT = .34f;
	public final static float WIDTH = .25f;
	public final static float HEIGHT = .32f;

	public final static int TIPO_JETPACK = 0;
	public final static int TIPO_BUBBLE = 1;
	public final static int TIPO_GUN = 2;
	public int tipo;

	public final Vector2 position;

	public float stateTime;

	public Item() {
		position = new Vector2();
	}

	public void init(float x, float y) {
		position.set(x, y);
		state = STATE_NORMAL;
		stateTime = 0;

		tipo = MathUtils.random(2);
	}

	public void update(float delta) {
		stateTime += delta;
	}

	public void take() {
		state = STATE_TAKEN;
		stateTime = 0;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
