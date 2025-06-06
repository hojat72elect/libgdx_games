package com.nopalsoft.ninjarunner.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The creature that follows our player throughout the game.
 */
public class Mascot {
	public final static int STATE_NORMAL = 0;
	public int state;

	public enum MascotType {
		PINK_BIRD, BOMB
	}

	public final MascotType mascotType;

	public final static float SPEED = 5f;

	public static final float RADIUS = .25f;

	public final float drawWidth;
	public final float drawHeight;

	public final float dashDrawWidth;
	public final float dashDrawHeight;

	public final Vector2 position;
	final public Vector2 targetPosition;
	public Vector2 velocity;
	public float stateTime;

	public Mascot(float x, float y, MascotType mascotType) {
		this.mascotType = mascotType;
		position = new Vector2(x, y);
		targetPosition = new Vector2(x, y);
		velocity = new Vector2();
		state = STATE_NORMAL;
		stateTime = 0;

		switch (mascotType) {
			case PINK_BIRD:
				drawWidth = .73f;
				drawHeight = .66f;
				dashDrawWidth = 2.36f;
				dashDrawHeight = 1.25f;
				break;
			default:
			case BOMB:
				drawWidth = dashDrawWidth = .52f;
				drawHeight = dashDrawHeight = .64f;
				break;
		}
	}

	public void update(Body body, float delta, float targetX, float targetY) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		targetPosition.set(targetX, targetY);

		velocity = body.getLinearVelocity();
		velocity.set(targetPosition).sub(position).scl(SPEED);
		body.setLinearVelocity(velocity);
		stateTime += delta;
	}

	public void updateStateTime(float delta) {
		stateTime += delta;
	}
}
