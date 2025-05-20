package com.nopalsoft.slamthebird.objetos;

import com.badlogic.gdx.math.Vector2;

public class Plataforma {
	public static float WIDTH = .75f;
	public static float HEIGHT = .2f;

	final public static int STATE_NORMAL = 0;
	final public static int STATE_CHANGING = 1;
	final public static int STATE_FIRE = 2;
	final public static int STATE_BREAKABLE = 3;
	final public static int STATE_BROKEN = 4;
	public int state;

	final float TIME_TO_BE_ACTIVE = 1.25f;
	public final float DURATION_ACTIVE = 10; // Este tiempo debe ser menor TIME_TO_CHANGE_STATE_PLATAFORM en la clase WorldGame

	public Vector2 position;
	public float stateTime;

	private boolean isFire, isBreakable;

	public float changinScale;// Para cuando cambie se vea una animacion// empieza en .5 para que no se aga todo chico

	public Plataforma(float x, float y) {
		position = new Vector2(x, y);
		state = STATE_NORMAL;
		changinScale = .5f;
	}

	public void update(float delta) {
		stateTime += delta;

		if (state == STATE_CHANGING) {
			changinScale = stateTime * 1f / TIME_TO_BE_ACTIVE;// 1.2 escala maxima

			if (stateTime >= TIME_TO_BE_ACTIVE) {
				if (isFire)
					state = STATE_FIRE;
				else if (isBreakable)
					state = STATE_BREAKABLE;
				stateTime = 0;

			}
		}

		if ((state == STATE_FIRE || state == STATE_BREAKABLE || state == STATE_BROKEN) && stateTime >= DURATION_ACTIVE) {
			isBreakable = isFire = false;
			state = STATE_NORMAL;
			stateTime = 0;
			changinScale = .5f;
		}
	}

	public void setFire() {
		state = STATE_CHANGING;
		isFire = true;
		stateTime = 0;
	}

	public void setBreakable() {
		state = STATE_CHANGING;
		isBreakable = true;
		stateTime = 0;
	}

	public void setBroken() {
		state = STATE_BROKEN;
		stateTime = 0;
	}

}
