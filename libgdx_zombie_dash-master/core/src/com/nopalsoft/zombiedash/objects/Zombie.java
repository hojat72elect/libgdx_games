package com.nopalsoft.zombiedash.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.game.WorldGame;

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

	float VELOCIDAD_WALK = .5f;

	public final static float DURATION_RISE = Assets.zombieKidRise.animationDuration + .2f;
	public final static float DURATION_DEAD = Assets.zombieKidDie.animationDuration + .2f;
	public final static float DURATION_HURT = .3f;

	public Vector2 position;
	public float stateTime;

	public boolean canUpdate;
	public boolean isFacingLeft;

	public final int MAX_LIFE;
	public int vidas;

	public Zombie(float x, float y, int tipo) {
		position = new Vector2(x, y);
		state = STATE_RISE;
		stateTime = 0;
		canUpdate = false;
		this.tipo = tipo;

		switch (tipo) {
		case TIPO_KID:
			vidas = 2;
			break;

		case TIPO_CUASY:
			vidas = 3;
			break;

		case TIPO_MUMMY:
			vidas = 4;
			break;
		case TIPO_PAN:
			vidas = 5;
			break;
		case TIPO_FRANK:
			vidas = 6;
			break;
		}
		MAX_LIFE = vidas;

		isFacingLeft = MathUtils.randomBoolean();

		if (isFacingLeft)
			VELOCIDAD_WALK = -VELOCIDAD_WALK;
	}

	public void update(float delta, Body body) {
		body.setAwake(true);
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		if (!canUpdate)
			return;
		Vector2 velocity = body.getLinearVelocity();

		velocity.x = WorldGame.velocidadX + VELOCIDAD_WALK;

		body.setLinearVelocity(velocity);

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

}
