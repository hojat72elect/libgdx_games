package com.nopalsoft.ninjarunner.objetos;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.ninjarunner.Assets;

public class Obstaculo implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	public final Vector2 position;
	public float stateTime;

	public PooledEffect effect;

	public Obstaculo() {
		position = new Vector2();
	}

	public void init(float x, float y) {
		position.set(x, y);
		state = STATE_NORMAL;
		stateTime = 0;

	}

	public void update(float delta) {
		stateTime += delta;
	}

	public void setDestroy() {
		if (state == STATE_NORMAL) {
			state = STATE_DESTROY;
			stateTime = 0;
			effect = Assets.cajasEffectPool.obtain();
			effect.setPosition(position.x, position.y);
		}
	}

	@Override
	public void reset() {
	}
}
