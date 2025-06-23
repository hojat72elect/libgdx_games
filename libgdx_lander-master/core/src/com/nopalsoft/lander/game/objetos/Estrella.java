package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Estrella {

	public static int STATE_NORMAL = 0;
	public static int STATE_TOMADA = 1;

	public Vector2 position;
	public Vector2 size;
	public float stateTime;

	public int state;

	public Estrella(float x, float y, float width, float height) {
		position = new Vector2(x, y);
		size = new Vector2(width, height);
		stateTime = 0;
		state = STATE_NORMAL;
	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		stateTime += delta;

	}

}
