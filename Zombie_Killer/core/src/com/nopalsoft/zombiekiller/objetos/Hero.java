package com.nopalsoft.zombiekiller.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;

public class Hero {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_HURT = 1;
	public final static int STATE_DEAD = 2;
	public int state;

	public final static int TIPO_FORCE = 0;
	public final static int TIPO_RAMBO = 1;
	public final static int TIPO_SOLDIER = 2;
	public final static int TIPO_SWAT = 3;
	public final static int TIPO_VADER = 4;
	public final int tipo;

	public static float VELOCIDAD_JUMP = 5;
	public static float VELOCIDAD_WALK = 1.5f;

	public final static float DURATION_DEAD = Assets.heroForceDie.animationDuration + .2f;
	public final static float DURATION_HURT = .5f;
	public final static float DURATION_IS_FIRING = Assets.heroForceShoot.animationDuration + .1f;

	public Vector2 position;
	public float stateTime;

	public boolean isFacingLeft;
	public boolean isWalking;
	public boolean isFiring;
	public boolean isClimbing;
	public boolean canJump;
	public Body bodyCrate;// Cuerpo del crate im standing

	/**
	 * Verdadero si toca las escaleras
	 */
	public boolean isOnStairs;

	public int vidas;
	public final int MAX_VIDAS = Settings.LEVEL_LIFE + 3;

	public int shield;
	public final int MAX_SHIELD = Settings.LEVEL_SHIELD + 1;

	public Hero(float x, float y, int tipo) {
		position = new Vector2(x, y);
		state = STATE_NORMAL;
		stateTime = 0;
		this.tipo = tipo;
		canJump = true;

		shield = MAX_SHIELD;
		vidas = MAX_VIDAS;

	}

	public void update(float delta, Body body, boolean didJump, float accelX, float accelY) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (state == STATE_HURT) {
			stateTime += delta;
			if (stateTime >= DURATION_HURT) {
				state = STATE_NORMAL;
				stateTime = 0;
			}
			return;
		}
		else if (state == STATE_DEAD) {
			stateTime += delta;
			return;
		}

		if (isFiring && stateTime >= DURATION_IS_FIRING) {
			isFiring = false;
			stateTime = 0;
		}

		Vector2 velocity = body.getLinearVelocity();

		if (didJump && canJump) {
			velocity.y = VELOCIDAD_JUMP;
			canJump = false;

			Assets.playSound(Assets.jump, 1);

		}

		if (isOnStairs) {
			if (accelY != 0) {
				isClimbing = true;
				body.setGravityScale(0);
			}

			if (accelY == 1) {
				velocity.y = 1;
			}
			else if (accelY == -1) {
				velocity.y = -1;
			}
			// Si no a empezado a escalar no limito la velocidad porque si salto y solo estoy en
			// las escaleras va caer con velocidad 0
			else if (isClimbing) {
				velocity.y = 0;
			}
		}
		else {
			body.setGravityScale(1);
			isClimbing = false;
		}

		if (accelX == -1) {
			velocity.x = -VELOCIDAD_WALK;
			isFacingLeft = true;
			isWalking = true;
		}
		else if (accelX == 1) {
			velocity.x = VELOCIDAD_WALK;
			isFacingLeft = false;
			isWalking = true;
		}
		else {
			if (bodyCrate != null)
				velocity.x = bodyCrate.getLinearVelocity().x;
			else
				velocity.x = 0;
			isWalking = false;
		}

		body.setLinearVelocity(velocity);

		if (isClimbing && accelY != 0)
			stateTime += delta;

		else if (!isClimbing)
			stateTime += delta;

	}

	public void getHurt() {
		if (state != STATE_NORMAL)
			return;

		if (shield > 0) {
			shield--;
			state = STATE_HURT;
			stateTime = 0;
			return;
		}

		vidas--;
		if (vidas > 0) {
			state = STATE_HURT;
		}
		else {
			state = STATE_DEAD;
		}
		stateTime = 0;
	}

	public void getShield() {
		shield += 2;
		if (shield > MAX_SHIELD)
			shield = MAX_SHIELD;
	}

	public void getHearth() {
		vidas += 1;
		if (vidas > MAX_VIDAS)
			vidas = MAX_VIDAS;
	}

	public void die() {
		if (state != STATE_DEAD) {
			vidas = 0;
			shield = 0;
			state = STATE_DEAD;
			stateTime = 0;
		}
	}

	public void fire() {
		isFiring = true;
		stateTime = 0;
	}

}
