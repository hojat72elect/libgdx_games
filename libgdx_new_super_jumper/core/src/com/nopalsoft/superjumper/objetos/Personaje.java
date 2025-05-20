package com.nopalsoft.superjumper.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.superjumper.screens.Screens;

public class Personaje {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DEAD = 1;
	public int state;

	public final static float DRAW_WIDTH = .75f;
	public final static float DRAW_HEIGTH = .8f;

	public final static float WIDTH = .4f;
	public final static float HEIGTH = .6f;

	final float VELOCIDAD_JUMP = 7.5f;
	final float VELOCIDAD_X = 5;

	public final float DURATION_BUBBLE = 3;
	public float durationBubble;

	public final float DURATION_JETPACK = 3;
	public float durationJetPack;

	final public Vector2 position;
	public Vector2 velocidad;
	public float angleDeg;

	public float stateTime;

	boolean didJump;
	public boolean isBubble;
	public boolean isJetPack;

	public Personaje(float x, float y) {
		position = new Vector2(x, y);
		velocidad = new Vector2();

		stateTime = 0;
		state = STATE_NORMAL;

	}

	public void update(Body body, float delta, float acelX) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		velocidad = body.getLinearVelocity();

		if (state == STATE_NORMAL) {

			if (didJump) {
				didJump = false;
				stateTime = 0;
				velocidad.y = VELOCIDAD_JUMP;

			}

			velocidad.x = acelX * VELOCIDAD_X;

			if (isBubble) {
				durationBubble += delta;
				if (durationBubble >= DURATION_BUBBLE) {
					durationBubble = 0;
					isBubble = false;
				}
			}

			if (isJetPack) {
				durationJetPack += delta;
				if (durationJetPack >= DURATION_JETPACK) {
					durationJetPack = 0;
					isJetPack = false;
				}
				velocidad.y = VELOCIDAD_JUMP;
			}

		}
		else {
			body.setAngularVelocity(MathUtils.degRad * 360);
			velocidad.x = 0;
		}

		body.setLinearVelocity(velocidad);

		if (position.x >= Screens.WORLD_WIDTH) {
			position.x = 0;
			body.setTransform(position, 0);
		}
		else if (position.x <= 0) {
			position.x = Screens.WORLD_WIDTH;
			body.setTransform(position, 0);
		}

		angleDeg = body.getAngle() * MathUtils.radDeg;

		velocidad = body.getLinearVelocity();
		stateTime += delta;

	}

	public void jump() {
		didJump = true;
	}

	public void hit() {
		if (state == STATE_NORMAL && !isBubble && !isJetPack) {
			state = STATE_DEAD;
			stateTime = 0;

		}
	}

	public void die() {
		if (state == STATE_NORMAL) {
			state = STATE_DEAD;
			stateTime = 0;
		}
	}

	public void setBubble() {
		if (state == STATE_NORMAL) {
			isBubble = true;
			durationBubble = 0;
		}
	}

	public void setJetPack() {
		if (state == STATE_NORMAL) {
			isJetPack = true;
			durationJetPack = 0;
		}
	}
}
