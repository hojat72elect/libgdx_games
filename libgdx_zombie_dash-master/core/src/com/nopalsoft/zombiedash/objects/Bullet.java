package com.nopalsoft.zombiedash.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.Settings;

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

	public Vector2 position;
	public float stateTime;

	public boolean isFacingLeft;
	public int DAMAGE = Settings.LEVEL_WEAPON + 1;

	public Bullet(float x, float y) {
		position = new Vector2(x, y);
		state = STATE_MUZZLE;

		tipo = LEVEL_0;

	}

	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

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
