package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Laser {

	public static int DIRECCION_HORIZONTAL = 0;
	public static int DIRECCION_VERTICAL = 1;

	public static float DRAW_ANCHO = .35f;

	float TIME_OFF = 5;
	public float timeOFF;

	float TIME_ON = 3;
	public float timeON;

	public static int STATE_NORMAL = 0;
	public static int STATE_FIRE = 1;

	public Vector2 position;

	public float stateTime;
	public int state;
	public int direccion;

	public final float width, height;

	public boolean isTouchingShip;

	public Laser(float x, float y, float width, float height, float tiempoEncendido, float tiempoApagado, float tiempoApagadoInicio, String direccion) {
		position = new Vector2(x, y);
		timeOFF = tiempoApagadoInicio;
		TIME_OFF = tiempoApagado;
		timeON = tiempoEncendido;
		stateTime = timeON = 0;
		state = STATE_NORMAL;

		if (height > width)
			width = DRAW_ANCHO;
		else
			height = DRAW_ANCHO;

		this.width = width;
		this.height = height;

		if (direccion.equals("horizontal"))
			this.direccion = DIRECCION_HORIZONTAL;
		else if (direccion.equals("vertical"))
			this.direccion = DIRECCION_VERTICAL;

	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (state == STATE_NORMAL) {
			timeOFF += delta;
			if (timeOFF >= TIME_OFF) {
				state = STATE_FIRE;
				timeOFF -= TIME_OFF;
				stateTime = 0;
			}
		}

		if (state == STATE_FIRE) {
			timeON += delta;
			if (timeON >= TIME_ON) {
				state = STATE_NORMAL;
				timeON -= TIME_ON;
				stateTime = 0;
			}
		}

		stateTime += delta;

	}
}
