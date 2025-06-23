package com.nopalsoft.zombiekiller.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Items {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_TAKEN = 1;
	public int state;

	public final float DRAW_WIDTH;
	public final float DRAW_HEIGHT;

	public Vector2 position;
	public float angleDeg;

	public float stateTime;

	public Items(float x, float y, float drawWidth, float drawHeight) {
		position = new Vector2(x, y);
		state = STATE_NORMAL;
		DRAW_HEIGHT = drawHeight;
		DRAW_WIDTH = drawWidth;
	}

	public void update(float delta, Body body) {
		if (body.getType() != BodyType.StaticBody) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;
			angleDeg = (float) Math.toDegrees(body.getAngle());
		}
		stateTime += delta;

	}

	public void taken() {
		state = STATE_TAKEN;
		stateTime = 0;

	}

}
