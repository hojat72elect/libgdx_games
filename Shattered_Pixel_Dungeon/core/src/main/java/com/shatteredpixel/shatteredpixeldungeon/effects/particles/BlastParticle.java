

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class BlastParticle extends PixelParticle.Shrinking {

	public static final Factory FACTORY = new Factory() {
		@Override
		public void emit(Emitter emitter, int index, float x, float y) {
			((BlastParticle) emitter.recycle(BlastParticle.class)).reset(x, y);
		}

		@Override
		public boolean lightMode() {
			return true;
		}
	};

	public BlastParticle() {
		super();

		color(0xEE7722);
		acc.set(0, +50);
	}

	public void reset(float x, float y) {
		revive();

		this.x = x;
		this.y = y;

		left = lifespan = Random.Float();

		size = 8;
		speed.polar(-Random.Float(3.1415926f), Random.Float(32, 64));
	}

	@Override
	public void update() {
		super.update();
		am = left > 0.8f ? (1 - left) * 5 : 1;
	}
}