package com.nopalsoft.zombiedash.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.Settings;

public class Hero {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_HURT = 1;
	public final static int STATE_DEAD = 2;
	public final static int STATE_REVIVE = 3;
	public int state;

	public final static int TIPO_FORCE = 0;
	public final static int TIPO_RAMBO = 1;
	public final static int TIPO_SOLDIER = 2;
	public final static int TIPO_SWAT = 3;
	public final static int TIPO_VADER = 4;
	public final int tipo;

	public static float VELOCIDAD_JUMP = 5;
	public final float VELOCIDAD_SECOND_JUMP;

	public final static float DURATION_DEAD = Assets.heroForceDie.animationDuration + .2f;
	public final static float DURATION_HURT = .5f;

	final Vector2 initialPosition;
	public Vector2 position;
	public float stateTime;

	public boolean isJumping;// To know if i can draw the jumping animation

	public int numPisosEnContacto;// Pisos que esta tocando actualmente si es ==0 no puede saltar

	private boolean canJump;
	private boolean canDoubleJump;

	public boolean didGetHurtAtLeastOnce;

	/**
	 * Verdadero si toca las escaleras
	 */

	public int vidas;
	public final int MAX_VIDAS = Settings.LEVEL_LIFE + 3;

	public int shield;
	public final int MAX_SHIELD = Settings.LEVEL_SHIELD + 1;

	public Hero(float x, float y, int tipo) {
		position = new Vector2(x, y);
		initialPosition = new Vector2(x, y);
		state = STATE_NORMAL;
		stateTime = 0;
		this.tipo = tipo;
		canJump = true;
		canDoubleJump = true;
		didGetHurtAtLeastOnce = false;

		shield = MAX_SHIELD;
		vidas = MAX_VIDAS;

		switch (Settings.LEVEL_SECOND_JUMP) {
		case 0:
			VELOCIDAD_SECOND_JUMP = 3.5f;
			break;
		case 1:
			VELOCIDAD_SECOND_JUMP = 4;
			break;
		case 2:
		case 3:
			VELOCIDAD_SECOND_JUMP = 4.35f;
			break;
		case 4:
		case 5:
			VELOCIDAD_SECOND_JUMP = 4.7f;
			break;
		case 6:
		default:
			VELOCIDAD_SECOND_JUMP = 5;
			break;
		}

	}

	public void update(float delta, Body body, boolean didJump, boolean isJumpPressed) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (state == STATE_REVIVE) {
			state = STATE_NORMAL;
			canJump = true;
			isJumping = false;
			canDoubleJump = true;
			stateTime = 0;
			vidas = MAX_VIDAS;
			initialPosition.y = 3;
			position.x = initialPosition.x;
			position.y = initialPosition.y;
			body.setTransform(initialPosition, 0);
			body.setLinearVelocity(0, 0);

		}
		else if (state == STATE_HURT) {
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

		Vector2 velocity = body.getLinearVelocity();

		if (didJump && (canJump || canDoubleJump)) {
			velocity.y = VELOCIDAD_JUMP;

			if (!canJump) {
				canDoubleJump = false;
				velocity.y = VELOCIDAD_SECOND_JUMP;
			}

			canJump = false;
			isJumping = true;
			stateTime = 0;
			body.setGravityScale(.9f);
			Assets.playSound(Assets.jump, 1);

		}
		if (!isJumpPressed)
			body.setGravityScale(1);

		stateTime += delta;

		body.setLinearVelocity(velocity);

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
		didGetHurtAtLeastOnce = true;
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

	public void touchFloor() {
		numPisosEnContacto++;

		canJump = true;
		isJumping = false;
		canDoubleJump = true;
		if (state == STATE_NORMAL)
			stateTime = 0;
	}

	public void endTouchFloor() {
		numPisosEnContacto--;
		if (numPisosEnContacto == 0) {
			canJump = false;

			// Si dejo de tocar el piso porque salto todavia puede saltar otra vez
			if (!isJumping)
				canDoubleJump = false;
		}

	}

	public void revive() {
		state = STATE_REVIVE;
		stateTime = 0;

	}
}
