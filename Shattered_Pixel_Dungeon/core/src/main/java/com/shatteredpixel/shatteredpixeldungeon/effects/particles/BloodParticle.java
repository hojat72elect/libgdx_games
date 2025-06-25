

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class BloodParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((BloodParticle) emitter.recycle(BloodParticle.class)).reset(x, y);
        }
    };

    public static final Emitter.Factory BURST = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((BloodParticle) emitter.recycle(BloodParticle.class)).resetBurst(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public BloodParticle() {
        super();

        color(0xCC0000);
        lifespan = 0.8f;

        acc.set(0, +40);
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        size = 4;
        speed.set(0);
    }

    public void resetBurst(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        speed.polar(Random.Float(PointF.PI2), Random.Float(16, 32));
        size = 5;

        left = 0.5f;
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.6f ? (1 - p) * 2.5f : 1;
    }
}