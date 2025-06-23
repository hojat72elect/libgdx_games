package com.nopalsoft.zombiedash.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiedash.game.WorldGame;

public class Spike {
	public final static int STATE_NORMAL = 0;
	public static int STATE_DESTROY = 1;
	public int state;

	final public float DRAW_WIDTH = 1f;
	final public float DRAW_HEIGHT = .2f;
	public Vector2 position;

	public float stateTime;

	public boolean didTouchLeft;
	public boolean didTouchRight;
	public boolean canDraw;

	public Spike(float x, float y) {
		position = new Vector2(x, y);
		state = STATE_NORMAL;

	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (didTouchLeft || didTouchRight) {
			if (didTouchLeft != didTouchRight)
				state = STATE_DESTROY;
			else {
				canDraw = true;
				body.setLinearVelocity(WorldGame.velocidadX, 0);
			}
		}

		stateTime += delta;
	}
}
