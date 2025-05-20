package com.nopalsoft.superjumper.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.superjumper.screens.Screens;

/**
 * LAs nubes son indestructibles Todas empizan happy hasta que les disparas
 * 
 * @author Yayo
 * 
 */
public class Nube implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DEAD = 1;
	public int state;

	public final static float DRAW_WIDTH = .95f;
	public final static float DRAW_HEIGHT = .6f;

	public final static float WIDTH = .65f;
	public final static float HEIGHT = .4f;

	public final static float VELOCIDAD_X = .5f;

	public final static int TIPO_HAPPY = 0;
	public final static int TIPO_ANGRY = 1;
	public int tipo;

	public final static float TIME_TO_BLOW = 2;
	public float timeToBlow;

	public final static float DURATION_BLOW = 3;
	public float durationBlow;

	public final static float TIME_FOR_RAYO = 5;
	public float timeForRayo;

	final public Vector2 position;
	public Vector2 velocidad;

	public boolean isBlowing;
	public boolean isLighthing;

	public float stateTime;

	public Nube() {
		position = new Vector2();
		velocidad = new Vector2();

	}

	public void init(float x, float y) {
		position.set(x, y);
		velocidad.set(0, 0);// La velocidad se la pongo desde el metodo donde la creo
		stateTime = 0;
		state = STATE_NORMAL;
		tipo = TIPO_HAPPY;

		isBlowing = isLighthing = false;

		timeToBlow = durationBlow = 0;
		timeForRayo = MathUtils.random(TIME_FOR_RAYO);
	}

	public void update(Body body, float delta) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		velocidad = body.getLinearVelocity();

		if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
			velocidad.x = velocidad.x * -1;
		}

		body.setLinearVelocity(velocidad);
		velocidad = body.getLinearVelocity();

		if (tipo == TIPO_ANGRY) {
			timeToBlow += delta;
			if (!isBlowing && timeToBlow >= TIME_TO_BLOW) {
				if (MathUtils.randomBoolean())
					isBlowing = true;
				timeToBlow = 0;
			}

			if (isBlowing) {
				durationBlow += delta;
				if (durationBlow >= DURATION_BLOW) {
					durationBlow = 0;
					isBlowing = false;
				}
			}
		}
		else {// TIPO HAPPY

			if (!isLighthing) {
				timeForRayo += delta;
				if (timeForRayo >= TIME_FOR_RAYO) {
					isLighthing = true;
				}
			}
		}

		stateTime += delta;

	}

	public void fireLighting() {
		isLighthing = false;
		timeForRayo = MathUtils.random(TIME_FOR_RAYO);
	}

	public void hit() {
		if (tipo == TIPO_HAPPY) {
			tipo = TIPO_ANGRY;
			stateTime = timeToBlow = durationBlow = 0;
		}
	}

	public void destroy() {
		if (state == STATE_NORMAL) {
			state = STATE_DEAD;
			stateTime = 0;
		}
	}

	@Override
	public void reset() {
	}

}
