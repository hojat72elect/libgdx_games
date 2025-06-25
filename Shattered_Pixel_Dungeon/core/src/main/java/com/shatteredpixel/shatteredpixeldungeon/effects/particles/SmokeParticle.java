

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SmokeParticle extends PixelParticle {

    public static final Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((SmokeParticle) emitter.recycle(SmokeParticle.class)).reset(x, y);
        }
    };

    public static final Factory SPEW = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((SmokeParticle) emitter.recycle(SmokeParticle.class)).resetSpew(x, y);
        }
    };

    public SmokeParticle() {
        super();

        color(0x222222);

        acc.set(0, -40);
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan = Random.Float(0.6f, 1f);
        speed.set(Random.Float(-4, +4), Random.Float(-8, +8));
    }

    public void resetSpew(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        acc.set(-40, 40);

        left = lifespan = Random.Float(0.6f, 1f);
        speed.polar(Random.Float(PointF.PI * 1.7f, PointF.PI * 1.8f), Random.Float(30, 60));
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        am = p > 0.8f ? 2 - 2 * p : p * 0.5f;
        size(16 - p * 8);
    }
}