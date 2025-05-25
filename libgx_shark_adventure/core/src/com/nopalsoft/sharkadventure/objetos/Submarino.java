package com.nopalsoft.sharkadventure.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.screens.Screens;

public class Submarino implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_EXPLODE = 1;
	public final static int STATE_REMOVE = 2;
	public int state;

	public final static float DURATION_EXPLOTION = .1f * 8f;
	public final static float VELOCIDDAD = 1.2f;

	public static float TIME_TO_FIRE;
	public float timeToFire;

	public final static float DRAW_WIDTH = 1.28f;
	public final static float DRAW_HEIGHT = 1.12f;

	public final static float WIDTH = 1.25f;
	public final static float HEIGHT = 1.09f;

	public final static int TIPO_AMARILLO = 0;
	public final static int TIPO_ROJO = 1;
	public int tipo;

	final public Vector2 targetPosition;
	final public Vector2 position;
	public Vector2 velocity;

	public float angleDeg;

	public float stateTime;
	public float explosionStateTimes[];
	public boolean didFire;

	int vida;

	public Submarino() {
		targetPosition = new Vector2();
		position = new Vector2();
		velocity = new Vector2();
		explosionStateTimes = new float[5];

	}

	public void init(float x, float y, float targetX, float targetY) {
		position.set(x, y);
		targetPosition.set(targetX, targetY);
		stateTime = 0;
		state = STATE_NORMAL;
		tipo = MathUtils.random(1);
		timeToFire = 0;
		TIME_TO_FIRE = MathUtils.random(1.25f, 2.75f);
		vida = 10;

		explosionStateTimes[0] = -1;
		explosionStateTimes[1] = -.5f;
		explosionStateTimes[2] = -.7f;
		explosionStateTimes[3] = 0;
		explosionStateTimes[4] = -.3f;

	}

	public void update(Body body, float delta) {

		velocity = body.getLinearVelocity();

		if (state == STATE_NORMAL) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;
			angleDeg = MathUtils.radDeg * body.getAngle();

			if (position.y < -4 || position.y > Screens.WORLD_HEIGHT + 4 || position.x < -4 || position.x > Screens.WORLD_WIDTH + 3)
				remove();

			velocity.set(targetPosition).sub(position).nor().scl(VELOCIDDAD);

			timeToFire += delta;
			if (timeToFire > TIME_TO_FIRE) {
				timeToFire -= TIME_TO_FIRE;
				didFire = true;
			}

		}
		else if (state == STATE_EXPLODE) {
			boolean remove = true;
			for (int i = 0; i < explosionStateTimes.length; i++) {
				explosionStateTimes[i] += delta;
				if (explosionStateTimes[i] < DURATION_EXPLOTION) {
					remove = false;
				}
			}

			if (remove) {
				state = STATE_REMOVE;
				stateTime = 0;
			}
		}

		body.setLinearVelocity(velocity);

		stateTime += delta;

	}

	public void hit() {
		if (state == STATE_NORMAL) {
			vida--;
			if (vida <= 0) {
				state = STATE_EXPLODE;
				stateTime = 0;
				if (Settings.isSoundOn) {
					Assets.playExplosionSound();
				}
			}
		}
	}

	public void remove() {
		state = STATE_REMOVE;
	}

	@Override
	public void reset() {
	}

}
