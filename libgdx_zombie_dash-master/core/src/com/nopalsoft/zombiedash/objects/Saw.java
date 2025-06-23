package com.nopalsoft.zombiedash.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiedash.game.WorldGame;

public class Saw {
	public final static int STATE_DIALOG = 0;
	public final static int STATE_NORMAL = 1;
	public static int STATE_DESTROY = 2;
	public int state;

	final float DURATION_STATE_DIALOG = 2;

	static public final float SIZE = .5f;

	final float velocityX = -2;

	public Vector2 position;
	public float angleDeg;

	public float stateTime;

	public Saw(float x, float y) {
		position = new Vector2(x, y);
		state = STATE_DIALOG;

	}

	public void update(float delta, Body body, Hero oHero) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (state == STATE_DIALOG) {
			if (stateTime >= DURATION_STATE_DIALOG) {
				state = STATE_NORMAL;
				stateTime = 0;
			}
			float y = oHero.position.y;
			body.setTransform(position.x, y, 0);
		}
		else {
			body.setLinearVelocity(WorldGame.velocidadX + velocityX, 0);
		}

		angleDeg = (float) Math.toDegrees(body.getAngle());
		stateTime += delta;
	}
}
