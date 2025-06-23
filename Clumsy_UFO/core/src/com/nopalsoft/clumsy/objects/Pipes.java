package com.nopalsoft.clumsy.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Pipes {
	public static int TIPO_ARRIBA = 0;
	public static int TIPO_ABAJO = 1;

	public static float WIDTH = .7f;
	public static float HEIGHT = 4f;

	public static int STATE_NORMAL = 0;
	public static int STATE_DESTROY = 1;

	public static float VELOCIDAD_X = -2f;

	public Vector2 position;
	public float stateTime;

	public int state;
	public int tipo;

	public Pipes(float x, float y, int tipo) {
		position = new Vector2(x, y);
		stateTime = 0;
		state = STATE_NORMAL;
		this.tipo = tipo;
	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		stateTime += delta;

	}

}
