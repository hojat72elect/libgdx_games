package com.nopalsoft.ninjarunner.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.ninjarunner.Assets;


public class Item implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	public final static float DURATION_PICK = Assets.pick.animationDuration + .1f;

	public final float WIDTH;
	public final float HEIGHT;

	public Vector2 velocity;
	public final Vector2 position;
	public float stateTime;

	public Item(float width, float height) {
		position = new Vector2();
		velocity = new Vector2();
		this.WIDTH = width;
		this.HEIGHT = height;

	}

	public void init(float x, float y) {
		position.set(x, y);
		velocity.set(0, 0);
		state = STATE_NORMAL;
		stateTime = 0;

	}

	public void update(float delta, Body body, Mascota oMascota, Personaje oPersonaje) {

		if (state == STATE_NORMAL) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;

			/** Primero checo si se atraen al personaje */
			if (oPersonaje.isMagnetEnabled && position.dst(oPersonaje.position) <= 5f) {
				moveCoinsMagenet(body, oPersonaje.position);

			}
			else if (oMascota != null && position.dst(oMascota.position) <= 2f) {
				// moveCoinsMagenet(body, oMascota.position);
			}
			else
				body.setLinearVelocity(0, 0);
		}

		stateTime += delta;
	}

	private void moveCoinsMagenet(Body body, Vector2 targetPosition) {
		velocity = body.getLinearVelocity();
		velocity.set(targetPosition).sub(position).scl(Personaje.VELOCIDAD_DASH + 3);
		body.setLinearVelocity(velocity);
	}

	public void setPicked() {
		if (state == STATE_NORMAL) {
			state = STATE_DESTROY;
			stateTime = 0;
		}
	}

	@Override
	public void reset() {
	}
}
