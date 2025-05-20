package com.nopalsoft.ninjarunner.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;


/**
 * Created by Yayo on 21/Jan/2015.
 */
public class Personaje {
	public final static int STATE_NORMAL = 0;// NORMAL APLICA PARA RUN,DASH,SLIDE,JUMP
	public final static int STATE_HURT = 1;
	public final static int STATE_DIZZY = 2;
	public final static int STATE_DEAD = 3;
	public final static int STATE_REVIVE = 4;
	public int state;

	public final static int TIPO_GIRL = 0;
	public final static int TIPO_BOY = 1;
	public final static int TIPO_NINJA = 2;
	public final int tipo;

	public final static float DRAW_WIDTH = 1.27f;
	public final static float DRAW_HEIGHT = 1.05f;

	public final static float WIDTH = .55f;
	public final static float HEIGHT = 1f;

	public final static float HEIGHT_SLIDE = .45f;

	public static final float VELOCIDAD_RUN = 3;
	public static final float VELOCIDAD_DASH = 7;

	public static float VELOCIDAD_JUMP = 5;
	public final float VELOCIDAD_SECOND_JUMP = 4;

	public final static float DURATION_DEAD = Assets.personajeDead.animationDuration + .5f;
	public final static float DURATION_HURT = Assets.personajeHurt.animationDuration + .1f;
	public final static float DURATION_DIZZY = 1.25f;

	final float DURATION_MAGNET;
	float durationMagnet;

	final float DURATION_DASH = 5;
	float durationDash;

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
	public final int MAX_VIDAS = Settings.LEVEL_LIFE + 5;

	public boolean isDash;
	public boolean isSlide;
	public boolean isIdle;

	/** Power **/
	public boolean isMagnetEnabled = false;

	public Personaje(float x, float y, int tipo) {
		position = new Vector2(x, y);
		initialPosition = new Vector2(x, y);
		state = STATE_NORMAL;
		stateTime = 0;
		this.tipo = tipo;
		canJump = true;
		canDoubleJump = true;
		didGetHurtAtLeastOnce = false;
		isIdle = true;

		vidas = MAX_VIDAS;
		DURATION_MAGNET = 10;

	}

	public void update(float delta, Body body, boolean didJump, boolean isJumpPressed, boolean dash, boolean didSlide) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		isIdle = false;

		// No importa si esta vivo/dizzy/ o lo que sea se le quita el tiempo
		if (isMagnetEnabled) {
			durationMagnet += delta;
			if (durationMagnet >= DURATION_MAGNET) {
				durationMagnet = 0;
				isMagnetEnabled = false;
			}
		}

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
		}
		else if (state == STATE_DIZZY) {
			stateTime += delta;
			body.setLinearVelocity(0, body.getLinearVelocity().y);
			if (stateTime >= DURATION_DIZZY) {
				state = STATE_NORMAL;
				stateTime = 0;
			}
			return;
		}
		else if (state == STATE_DEAD) {
			stateTime += delta;
			body.setLinearVelocity(0, body.getLinearVelocity().y);
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

			isSlide = false;

			body.setGravityScale(.9f);
			Assets.playSound(Assets.jump, 1);// FIXME Arreglar el sonido

		}
		if (!isJumpPressed)
			body.setGravityScale(1);

		if (!isJumping) {
			isSlide = didSlide;
		}

		// DASH
		if (dash) {
			isDash = true;
			durationDash = 0;
		}

		if (isDash) {
			durationDash += delta;
			velocity.x = VELOCIDAD_DASH;
			if (durationDash >= DURATION_DASH) {
				isDash = false;
				stateTime = 0;
				velocity.x = VELOCIDAD_RUN;
			}
		}
		else {
			velocity.x = VELOCIDAD_RUN;
		}
		stateTime += delta;

		body.setLinearVelocity(velocity);

	}

	public void getHurt() {
		if (state != STATE_NORMAL)
			return;

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

	public void getDizzy() {
		if (state != STATE_NORMAL)
			return;

		vidas--;
		if (vidas > 0) {
			state = STATE_DIZZY;
		}
		else {
			state = STATE_DEAD;
		}
		stateTime = 0;
		didGetHurtAtLeastOnce = true;
	}

	public void getHearth() {
		vidas += 1;
		if (vidas > MAX_VIDAS)
			vidas = MAX_VIDAS;
	}

	public void die() {
		if (state != STATE_DEAD) {
			vidas = 0;

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

	public void updateStateTime(float delta) {
		stateTime += delta;
	}

	public void setPickUpMagnet() {
		durationMagnet = 0;
		isMagnetEnabled = true;

	}
}
