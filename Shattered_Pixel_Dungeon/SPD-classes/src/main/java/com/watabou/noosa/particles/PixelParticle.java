

package com.watabou.noosa.particles;

import com.watabou.noosa.Game;
import com.watabou.noosa.PseudoPixel;

public class PixelParticle extends PseudoPixel {

	protected float size;

	protected float lifespan;
	protected float left;

	public PixelParticle() {
		super();

		origin.set(+0.5f);
	}

	public void reset(float x, float y, int color, float size, float lifespan) {
		revive();

		this.x = x;
		this.y = y;

		color(color);
		size(this.size = size);

		this.left = this.lifespan = lifespan;
	}

	@Override
	public void update() {
		super.update();

		if ((left -= Game.elapsed) <= 0) {
			kill();
		}
	}

	public static class Shrinking extends PixelParticle {
		@Override
		public void update() {
			super.update();
			size(size * left / lifespan);
		}
	}
}
