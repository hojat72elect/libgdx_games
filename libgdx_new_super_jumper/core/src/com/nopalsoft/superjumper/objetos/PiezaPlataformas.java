package com.nopalsoft.superjumper.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PiezaPlataformas implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	public static final float DRAW_WIDTH_NORMAL = Plataformas.DRAW_WIDTH_NORMAL / 2f;
	public static final float DRAW_HEIGTH_NORMAL = Plataformas.DRAW_HEIGTH_NORMAL;
	public static final float WIDTH_NORMAL = Plataformas.WIDTH_NORMAL / 2f;
	public static final float HEIGTH_NORMAL = Plataformas.HEIGTH_NORMAL;

	public static final int COLOR_BEIGE = 0;
	public static final int COLOR_BLUE = 1;
	public static final int COLOR_GRAY = 2;
	public static final int COLOR_GREEN = 3;
	public static final int COLOR_MULTICOLOR = 4;
	public static final int COLOR_PINK = 5;
	public int color;

	public static final int TIPO_LEFT = 0;
	public static final int TIPO_RIGHT = 1;
	public int tipo;

	public final Vector2 position;

	public float stateTime;
	public float angleDeg;

	public PiezaPlataformas() {
		position = new Vector2();

	}

	public void init(float x, float y, int tipo, int color) {
		position.set(x, y);
		this.tipo = tipo;
		this.color = color;
		angleDeg = 0;
		stateTime = 0;
		state = STATE_NORMAL;
	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		angleDeg = MathUtils.radiansToDegrees * body.getAngle();

		if (angleDeg > 90) {
			body.setTransform(position, MathUtils.degreesToRadians * 90);
			angleDeg = 90;
		}

		stateTime += delta;

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public void setDestroy() {
		state = STATE_DESTROY;
	}
}
