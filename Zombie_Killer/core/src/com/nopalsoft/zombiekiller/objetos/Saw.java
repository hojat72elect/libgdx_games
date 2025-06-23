package com.nopalsoft.zombiekiller.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Saw {
	public final static int STATE_NORMAL = 0;
	public int state;

	public final float SIZE;

	public Vector2 position;
	public float angleDeg;

	public float stateTime;

	public Saw(float x, float y, float size) {
		position = new Vector2(x, y);
		state = STATE_NORMAL;
		SIZE = size;

	}

	public void update(float delta, Body body) {

		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		angleDeg = (float) Math.toDegrees(body.getAngle());
		stateTime += delta;
	}
}
