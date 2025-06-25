

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;

public class SacrificialParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((SacrificialParticle) emitter.recycle(SacrificialParticle.class)).reset(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public SacrificialParticle() {
        super();

        color(0x4488EE);
        lifespan = 0.6f;

        acc.set(0, -100);
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y - 4;

        left = lifespan;

        size = 4;
        speed.set(0);
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.75f ? (1 - p) * 4 : 1;
    }
}