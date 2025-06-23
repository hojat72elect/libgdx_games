package com.nopalsoft.zombiedash.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Pisable {
	public static int STATE_NORMAL = 0;
	public static int STATE_DESTROY = 1;
	public int state;

	public Vector2 position;
	public float stateTime;

	public float DRAW_WIDTH, DRAW_HEIGHT;

	public Pisable(float x, float y, float drawWidth, float drawHeight) {
		position = new Vector2(x, y);
		stateTime = 0;
		state = STATE_NORMAL;
		DRAW_WIDTH = drawWidth;
		DRAW_HEIGHT = drawHeight;
	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		stateTime += delta;

	}

}
