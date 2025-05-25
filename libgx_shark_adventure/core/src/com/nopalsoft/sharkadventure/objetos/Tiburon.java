package com.nopalsoft.sharkadventure.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;

public class Tiburon implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DEAD = 1;
	public int state;

	final float VELOCIDAD_X = 3.5f;
	final float VELOCIDAD_Y = 1.85f;

	public final static int MAX_LIFE = 5;
	public final static int MAX_ENERGY = 50;
	public int life;
	public float energy;

	public final static float DURATION_TURBO = 3;
	float durationTurbo;

	public final static float DURATION_FIRING = .075f * 5;

	public final float TIME_TO_RECHARGE_ENERGY = 1.25f;
	float timeToRechargeEnergy;

	final public Vector2 position;
	public Vector2 velocidad;
	public float angleDeg;
	public float stateTime;

	public boolean isTurbo;
	public boolean isFacingLeft;
	public boolean isFiring;

	boolean setSpeedDie;

	public boolean didGetHurtOnce;

	/** Indica si se acaba de hacer un flip en X para recrear el cuerpo */
	public boolean didFlipX;

	public Tiburon(float x, float y) {
		position = new Vector2(x, y);
		velocidad = new Vector2();

		stateTime = 0;
		state = STATE_NORMAL;

		life = MAX_LIFE;
		energy = MAX_ENERGY;

		setSpeedDie = true;
		didGetHurtOnce = false;

	}

	public void updateStateTime(float delta) {
		stateTime += delta;
	}

	public void update(Body body, float delta, float accelX, boolean didSwimUp) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		velocidad = body.getLinearVelocity();

		if (state == STATE_NORMAL) {

			if (isTurbo) {
				durationTurbo += delta;
				if (durationTurbo >= DURATION_TURBO) {
					durationTurbo = 0;
					isTurbo = false;
				}
			}

			if (isFiring) {
				if (stateTime >= DURATION_FIRING) {
					stateTime = 0;
					isFiring = false;
				}
			}

			velocidad.x = accelX * VELOCIDAD_X;

			if (velocidad.x > 0 && isFacingLeft) {
				didFlipX = true;
				isFacingLeft = false;
			}
			else if (velocidad.x < 0 && !isFacingLeft) {
				didFlipX = true;
				isFacingLeft = true;
			}

			if (didSwimUp) {
				velocidad.y = VELOCIDAD_Y;

				if (Settings.isSoundOn)
					Assets.sSwim.play();
			}

			timeToRechargeEnergy += delta;
			if (timeToRechargeEnergy >= TIME_TO_RECHARGE_ENERGY) {
				timeToRechargeEnergy -= TIME_TO_RECHARGE_ENERGY;
				energy += 1.5f;
				if (energy > MAX_ENERGY)
					energy = MAX_ENERGY;
			}

		}
		else {

			if (setSpeedDie) {
				velocidad.set(0, 0);
				setSpeedDie = false;
			}

			body.setGravityScale(-.15f);
			body.setAngularVelocity(MathUtils.degreesToRadians * 90);
			angleDeg = MathUtils.radDeg * body.getAngle();

			if (angleDeg >= 180) {
				body.setAngularVelocity(0);
				angleDeg = 180;

			}

		}

		body.setLinearVelocity(velocidad);
		stateTime += delta;

	}

	public void fire() {
		if (state == STATE_NORMAL) {
			isFiring = true;
			stateTime = 0;
			energy -= 1.25f;
			if (energy < 0)
				energy = 0;
		}
	}

	public void hit() {
		if (state == STATE_NORMAL) {
			life--;
			if (life == 0) {
				state = STATE_DEAD;
				stateTime = 0;

			}
			didGetHurtOnce = true;
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
}
