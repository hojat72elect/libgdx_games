package com.nopalsoft.slamthebird.objetos;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.slamthebird.Settings;

public class Enemigo {
	public static float WIDTH = .4f;
	public static float HEIGHT = .4f;

	public static int STATE_JUST_APPEAR = 0;
	public static int STATE_FLYING = 1;
	public static int STATE_HIT = 2;
	public static int STATE_EVOLVING = 3;// PAra que vuele otra vez
	public static int STATE_DEAD = 4;

	public float TIME_JUST_APPEAR = 1.7f;

	public static float MAX_VELOCIDAD_AZUL = 1.75f;
	public static float MAX_VELOCIDAD_ROJO = 3.25f;

	public float TIME_TO_CHANGE_VEL = 3;
	public float timeToChangeVel;

	public float TIME_TO_EVOLVE = 3f;
	public float timeToEvolve;

	public float DURARTION_EVOLVING = 1.5f;

	public float DURARTION_FROZEN = 5f;
	float durationFrozen;

	public Vector2 position;

	public Vector2 velocidad;

	public boolean isFrozen;

	public int state;
	public float stateTime;

	public int vidas;

	public float appearScale;

	public Enemigo(float x, float y) {
		position = new Vector2(x, y);
		state = STATE_JUST_APPEAR;
		vidas = 2;
		stateTime = 0;
		velocidad = new Vector2();
		isFrozen = false;
		durationFrozen = 0;
		DURARTION_FROZEN += Settings.NIVEL_BOOST_ICE;

	}

	public void update(float delta, Body body, Random oRan) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (isFrozen) {
			body.setLinearVelocity(0, 0);
			if (durationFrozen >= DURARTION_FROZEN) {
				isFrozen = false;
				durationFrozen = 0;
				setNewVelocity(body, oRan, false);
			}
			durationFrozen += delta;
			return;// Ya no se hace nada mas si esta congelado. No se mueve, no cambia de velocidad, no evoluciona, no nada.
		}

		// Pase lo que pase no quiero que este mas arriba de 10f
		if (position.y > 10f) {
			velocidad = body.getLinearVelocity();
			body.setLinearVelocity(velocidad.x, velocidad.y * -1);
		}
		if (state == STATE_JUST_APPEAR) {
			appearScale = stateTime * 1.5f / TIME_JUST_APPEAR;// 1.5f escala maxima

			if (stateTime >= TIME_JUST_APPEAR) {
				state = STATE_FLYING;
				stateTime = 0;
				setNewVelocity(body, oRan, false);
			}
		}

		if (state != STATE_JUST_APPEAR) {

			timeToChangeVel += delta;
			if (timeToChangeVel >= TIME_TO_CHANGE_VEL) {
				timeToChangeVel -= TIME_TO_CHANGE_VEL;

				Vector2 vel = body.getLinearVelocity();

				// Cambio en X
				if (oRan.nextBoolean())
					vel.x *= -1;

				if (state == STATE_FLYING) {
					if (oRan.nextBoolean())
						vel.y *= -1;
				}
				body.setLinearVelocity(vel);
			}
		}

		if (state == STATE_HIT) {
			body.setGravityScale(1);
			timeToEvolve += delta;
			if (timeToEvolve >= TIME_TO_EVOLVE) {
				state = STATE_EVOLVING;
				stateTime = 0;
				timeToEvolve = 0;
			}
		}

		if (state == STATE_EVOLVING && stateTime >= DURARTION_EVOLVING) {
			state = STATE_FLYING;
			body.setGravityScale(0);
			setNewVelocity(body, oRan, true);
			vidas = 3;
			stateTime = 0;
		}

		velocidad = body.getLinearVelocity();

		controlarVelocidad(body);
		velocidad = body.getLinearVelocity();

		stateTime += delta;
	}

	/*
	 * Limita la velocidad porque a veces la fuerza resultande te la colision ponia loco al enemigo
	 */
	private void controlarVelocidad(Body body) {
		float vel = MAX_VELOCIDAD_AZUL;
		if (vidas == 3)
			vel = MAX_VELOCIDAD_ROJO;

		if (velocidad.x > vel) {
			velocidad.x = vel;
		}
		else if (velocidad.x < -vel) {
			velocidad.x = -vel;
		}

		if (vidas > 1) {// Asi el pajaro cai rapido si le quito las alas
			if (velocidad.y > vel) {
				velocidad.y = vel;
			}
			else if (velocidad.y < -vel) {
				velocidad.y = -vel;
			}
		}
		body.setLinearVelocity(velocidad);

	}

	/**
	 * Si esta tocando el piso hago que la velocidad en Y siempre se genere positiva
	 * 
	 * @param body
	 * @param oRan
	 * @param isTouchingFLoor
	 */
	private void setNewVelocity(Body body, Random oRan, boolean isTouchingFLoor) {
		float vel = MAX_VELOCIDAD_AZUL;
		if (vidas == 3)
			vel = MAX_VELOCIDAD_ROJO;

		float velX = oRan.nextFloat() * vel * 2 - vel;
		float velY;
		if (isTouchingFLoor)
			velY = oRan.nextFloat() * vel;
		else
			velY = oRan.nextFloat() * vel * 2 - vel;

		body.setLinearVelocity(velX, velY);
	}

	public void hit() {
		vidas--;
		if (vidas == 1)
			state = STATE_HIT;
		else if (vidas == 0)
			state = STATE_DEAD;

		stateTime = 0;

	}

	public void die() {
		vidas = 0;
		state = STATE_DEAD;
		stateTime = 0;
	}

	public void setFrozen() {
		durationFrozen = 0;
		isFrozen = true;
	}

}
