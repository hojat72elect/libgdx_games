

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class ShaftParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((ShaftParticle) emitter.recycle(ShaftParticle.class)).reset(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public ShaftParticle() {
        super();

        lifespan = 1.2f;
        speed.set(0, -6);
    }

    private float offs;

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        offs = -Random.Float(lifespan);
        left = lifespan - offs;
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        am = p < 0.5f ? p : 1 - p;
        scale.x = (1 - p) * 4;
        scale.y = 16 + (1 - p) * 16;
    }
}