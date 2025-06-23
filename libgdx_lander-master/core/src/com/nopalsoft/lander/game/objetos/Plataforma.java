package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Plataforma {

	public boolean isFinal;
	public static int STATE_NORMAL = 0;
	public static int STATE_DESTROY = 1;

	public static float VELOCIDAD_X = -2f;

	public Vector2 position;
	public Vector2 size;
	public float stateTime;

	public int state;

	public Plataforma(float x, float y, float width, float height) {
		position = new Vector2(x, y);
		size = new Vector2(width, height);
		stateTime = 0;
		state = STATE_NORMAL;
		isFinal = false;
	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		stateTime += delta;

	}

}
