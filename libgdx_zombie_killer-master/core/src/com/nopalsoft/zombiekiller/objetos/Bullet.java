package com.nopalsoft.zombiekiller.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;

public class Bullet {
	public final static int STATE_MUZZLE = 0;
	public final static int STATE_NORMAL = 1;
	public final static int STATE_HIT = 2;
	public final static int STATE_DESTROY = 3;
	public int state;

	public final static int LEVEL_0 = 0;
	public final static int LEVEL_1 = 1;
	public final static int LEVEL_2 = 2;
	public final static int LEVEL_3 = 3;
	public final static int LEVEL_4_AND_UP = 4;
	public final int tipo;

	public static float VELOCIDAD = 5;

	public final static float DURATION_MUZZLE = Assets.muzzle.animationDuration;
	public final float FORCE_IMPACT;
	public final int DAMAGE;

	public Vector2 position;
	public float stateTime;

	public boolean isFacingLeft;

	public Bullet(float x, float y, boolean isFacingLeft) {
		position = new Vector2(x, y);
		state = STATE_MUZZLE;
		this.isFacingLeft = isFacingLeft;

		switch (Settings.LEVEL_WEAPON) {
		case LEVEL_0:
			FORCE_IMPACT = 0f;
			DAMAGE = 1;
			tipo = LEVEL_0;
			break;
		case LEVEL_1:
			FORCE_IMPACT = .075f;
			DAMAGE = 2;
			tipo = LEVEL_1;
			break;
		case LEVEL_2:
			FORCE_IMPACT = .1f;
			DAMAGE = 3;
			tipo = LEVEL_2;
			break;
		case LEVEL_3:
			FORCE_IMPACT = .25f;
			DAMAGE = 4;
			tipo = LEVEL_3;
			break;
		default:
			if (Settings.LEVEL_WEAPON == 4) {
				FORCE_IMPACT = .5f;
				DAMAGE = 5;
			}
			else if (Settings.LEVEL_WEAPON == 5) {
				FORCE_IMPACT = .75f;
				DAMAGE = 6;
			}
			else {
				FORCE_IMPACT = 1f;
				DAMAGE = 7;
			}
			tipo = LEVEL_4_AND_UP;
			break;
		}

		Gdx.app.log("Weapon level", Settings.LEVEL_WEAPON + "");
		Gdx.app.log("Damage", DAMAGE + "");
		Gdx.app.log("Force impact ", FORCE_IMPACT + "");
	}

	public void update(float delta, Body body) {
		if (state == STATE_MUZZLE || state == STATE_NORMAL) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;
		}

		if (state == STATE_HIT)
			body.setLinearVelocity(0, 0);

		if (state == STATE_MUZZLE || state == STATE_HIT) {
			stateTime += delta;
			if (stateTime >= DURATION_MUZZLE) {
				if (state == STATE_MUZZLE)
					state = STATE_NORMAL;
				else if (state == STATE_HIT)
					state = STATE_DESTROY;
				stateTime = 0;
			}
			return;
		}

		stateTime += delta;
	}

	public void hit() {
		state = STATE_HIT;
		stateTime = 0;
	}

}
