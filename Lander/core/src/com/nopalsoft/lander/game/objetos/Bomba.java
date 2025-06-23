package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bomba {

	final static public float VELOCIDAD = 1f;

	public static int DIRECCION_UP = 0;
	public static int DIRECCION_RIGHT = 1;

	final public static int STATE_NORMAL = 0;
	final public static int STATE_TOMADA = 1;
	final public static int STATE_EXPLOSION = 2;

	final float TIME_TOMADA = .5f;
	final public static float TIME_EXPLOSION = .05f * 20;;

	public Vector2 position;
	public Vector2 velocidad;

	public float stateTime;

	public int state;
	public int direccion;

	public Bomba(float x, float y, String direccion) {
		position = new Vector2(x, y);
		stateTime = 0;
		state = STATE_NORMAL;

		if (direccion.equals("arriba")) {
			this.direccion = DIRECCION_UP;
			velocidad = new Vector2(0, VELOCIDAD);
		}
		else if (direccion.equals("abajo")) {
			this.direccion = DIRECCION_UP;
			velocidad = new Vector2(0, -VELOCIDAD);
		}
		else if (direccion.equals("derecha")) {
			this.direccion = DIRECCION_RIGHT;
			velocidad = new Vector2(VELOCIDAD, 0);
		}

		else if (direccion.equals("izquierda")) {
			this.direccion = DIRECCION_RIGHT;
			velocidad = new Vector2(-VELOCIDAD, 0);
		}
	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (state == STATE_TOMADA && stateTime > TIME_TOMADA) {
			state = STATE_EXPLOSION;
			stateTime = 0;
		}

		stateTime += delta;

	}

	public void tomada() {
		state = STATE_TOMADA;
		stateTime = 0;
	}

	public void cambioDireccion() {
		velocidad.x *= -1;
		velocidad.y *= -1;
	}

}
