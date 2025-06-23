package com.nopalsoft.zombiekiller.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiekiller.Assets;

public class Zombie {
	public final static int STATE_RISE = 0;
	public final static int STATE_NORMAL = 1;
	public final static int STATE_HURT = 2;
	public final static int STATE_DEAD = 3;
	public int state;

	public final static int TIPO_KID = 0;
	public final static int TIPO_FRANK = 1;
	public final static int TIPO_CUASY = 2;
	public final static int TIPO_PAN = 3;
	public final static int TIPO_MUMMY = 4;
	public final int tipo;

	public float VELOCIDAD_WALK;

	public final static float DURATION_RISE = Assets.zombieKidRise.animationDuration + .2f;
	public final static float DURATION_DEAD = Assets.zombieKidDie.animationDuration + .2f;
	public final static float DURATION_HURT = .3f;
	public float FORCE_IMPACT;

	public Vector2 position;
	public float stateTime;

	public boolean isFacingLeft;
	public boolean isWalking;
	public boolean canUpdate;
	public boolean isFollowing;

	public final int MAX_LIFE;
	public int vidas;

	public final float TIME_TO_HURT_PLAYER = 1;
	float timeToHurtPlayer;

	public boolean isTouchingPlayer;

	public Zombie(float x, float y, int tipo) {
		position = new Vector2(x, y);
		state = STATE_RISE;
		stateTime = 0;
		this.tipo = tipo;
		canUpdate = false;

		isFollowing = true;

		switch (tipo) {
		case TIPO_KID:
			vidas = 5;
			FORCE_IMPACT = 2.5f;
			VELOCIDAD_WALK = 1.1f;
			break;

		case TIPO_CUASY:
			vidas = 15;
			FORCE_IMPACT = 3;
			VELOCIDAD_WALK = .5f;
			break;

		case TIPO_MUMMY:
			vidas = 100;
			FORCE_IMPACT = 8;
			VELOCIDAD_WALK = .5f;
			break;
		case TIPO_PAN:
			vidas = 50;
			FORCE_IMPACT = 4;
			VELOCIDAD_WALK = .7f;
			break;
		case TIPO_FRANK:
			vidas = 120;
			FORCE_IMPACT = 5;
			VELOCIDAD_WALK = 1.3f;
			break;
		}
		MAX_LIFE = vidas;
	}

	public void update(float delta, Body body, float accelX, Hero oHero) {
		body.setAwake(true);
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		Vector2 velocity = body.getLinearVelocity();

		// PAra que si no se pueden actuzliar al menos se caigan los objetos y queden al ras del suelo
		// Entonces la tierra se ve pegada al suelo cuando state==Rise
		if (!canUpdate) {
			body.setLinearVelocity(0, velocity.y);
			return;
		}

		if (oHero.position.x > position.x)
			isFacingLeft = false;
		else
			isFacingLeft = true;

		if (state == STATE_RISE) {
			stateTime += delta;
			if (stateTime >= DURATION_RISE) {
				state = STATE_NORMAL;
				stateTime = 0;
			}
			return;
		}
		else if (state == STATE_DEAD) {
			stateTime += delta;
			return;
		}
		else if (state == STATE_HURT) {
			stateTime += delta;
			if (stateTime >= DURATION_HURT) {
				state = STATE_NORMAL;
				stateTime = 0;
			}
			// body.setLinearVelocity(0, velocity.y);
			return;
		}

		if (isTouchingPlayer) {
			timeToHurtPlayer += delta;
			if (timeToHurtPlayer >= TIME_TO_HURT_PLAYER) {
				timeToHurtPlayer -= TIME_TO_HURT_PLAYER;
				oHero.getHurt();
			}
		}
		else {
			timeToHurtPlayer = 0;
		}

		if (isFollowing) {
			if (oHero.position.x + .1f < position.x)
				accelX = -1;
			else if (oHero.position.x - .1f > position.x)
				accelX = 1;
			else
				accelX = 0;
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
			velocity.x = 0;
			isWalking = false;
		}

		body.setLinearVelocity(velocity);

		stateTime += delta;
	}

	public void getHurt(int damage) {
		if (state == STATE_NORMAL || state == STATE_HURT) {
			vidas -= damage;
			if (vidas <= 0) {
				state = STATE_DEAD;
				stateTime = 0;
			}
			else {
				if (state == STATE_NORMAL) {
					state = STATE_HURT;
					stateTime = 0;
				}
			}

		}
	}

	public void die() {
		if (state != STATE_DEAD) {
			state = STATE_DEAD;
			stateTime = 0;
		}
	}

}
