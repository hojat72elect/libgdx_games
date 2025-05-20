package com.nopalsoft.slamthebird.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;

public class Robot {
	public static float RADIUS = .28f;

	public static int STATE_FALLING = 0;
	public static int STATE_JUMPING = 1;
	public static int STATE_DEAD = 2;

	public float VELOCIDAD_JUMP = 6.25f;
	public float VELOCIDAD_MOVE = 5f;

	public float DURATION_SUPER_JUMP = 5;
	public float durationSuperJump;

	public float DURATION_INVENCIBLE = 5;
	public float durationInvencible;

	public static final float DURATION_DEAD_ANIMATION = 2;

	public Vector2 position;

	public int state;
	public float stateTime;

	public boolean jump, slam;
	public boolean isSuperJump;
	public boolean isInvencible;

	public float angleGrad;
	public Vector2 velocidad;

	public Robot(float x, float y) {
		position = new Vector2(x, y);
		state = STATE_JUMPING;
		velocidad = new Vector2();
		jump = true;// para que haga el primer salto

		DURATION_SUPER_JUMP += Settings.NIVEL_BOOST_SUPER_JUMP;
		DURATION_INVENCIBLE += Settings.NIVEL_BOOST_INVENCIBLE;

	}

	public void update(float delta, Body body, float acelX, boolean slam) {
		this.slam = slam;// Para dibujar la caida rapida =)
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		angleGrad = 0;
		if (state == STATE_FALLING || state == STATE_JUMPING) {

			if (slam)
				body.setGravityScale(2.5f);
			else
				body.setGravityScale(1);

			if (jump) {
				jump = false;
				state = STATE_JUMPING;
				stateTime = 0;
				if (isSuperJump) {
					body.setLinearVelocity(body.getLinearVelocity().x,
							VELOCIDAD_JUMP + 3);
				}
				else {
					body.setLinearVelocity(body.getLinearVelocity().x,
							VELOCIDAD_JUMP);
				}
			}

			Vector2 velocidad = body.getLinearVelocity();

			if (velocidad.y < 0 && state != STATE_FALLING) {
				state = STATE_FALLING;
				stateTime = 0;
			}
			body.setLinearVelocity(acelX * VELOCIDAD_MOVE, velocidad.y);

			if (isSuperJump) {
				durationSuperJump += delta;
				if (durationSuperJump >= DURATION_SUPER_JUMP) {
					isSuperJump = false;
					durationSuperJump = 0;
				}
			}

			if (isInvencible) {
				durationInvencible += delta;
				if (durationInvencible >= DURATION_INVENCIBLE) {
					isInvencible = false;
					durationInvencible = 0;
				}
			}
		}
		else if (state == STATE_DEAD) {
			body.setLinearVelocity(0, -3);
			body.setFixedRotation(false);
			angleGrad = (float) Math.toDegrees(body.getAngle());
			// if (body.getAngularVelocity() != 0)
			body.setAngularVelocity((float) Math.toRadians(20));
		}
		velocidad = body.getLinearVelocity();
		stateTime += delta;
	}

	public void updateReady(float delta, Body body, float acelX) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		body.setLinearVelocity(acelX * VELOCIDAD_MOVE, 0);
		velocidad = body.getLinearVelocity();
	}

	public void jump() {
		if (state == STATE_FALLING) {
			jump = true;
			stateTime = 0;
			Assets.playSound(Assets.soundJump);
		}
	}

	/**
	 * El robot es golpeado y muere
	 */
	public void hit() {
		state = STATE_DEAD;
		stateTime = 0;
	}

}
