

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class PoisonParticle extends PixelParticle {

    public static final Emitter.Factory MISSILE = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((PoisonParticle) emitter.recycle(PoisonParticle.class)).resetMissile(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public static final Emitter.Factory SPLASH = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((PoisonParticle) emitter.recycle(PoisonParticle.class)).resetSplash(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public PoisonParticle() {
        super();

        lifespan = 0.6f;

        acc.set(0, +30);
    }

    public void resetMissile(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        speed.polar(-Random.Float(3.1415926f), Random.Float(6));
    }

    public void resetSplash(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        speed.polar(Random.Float(3.1415926f), Random.Float(10, 20));
    }

    @Override
    public void update() {
        super.update();
        // alpha: 1 -> 0; size: 1 -> 4
        size(4 - (am = left / lifespan) * 3);
        // color: 0x8844FF -> 0x00FF00
        color(ColorMath.interpolate(0x00FF00, 0x8844FF, am));
    }
}