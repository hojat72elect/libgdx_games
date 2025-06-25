

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class PitfallParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY4 = new Emitter.Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((PitfallParticle) emitter.recycle(PitfallParticle.class)).reset(x, y, 4);
        }
    };

    public static final Emitter.Factory FACTORY8 = new Emitter.Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((PitfallParticle) emitter.recycle(PitfallParticle.class)).reset(x, y, 8);
        }
    };

    public PitfallParticle() {
        super();

        color(0x000000);
        angle = Random.Float(-30, 30);
    }

    public void reset(float x, float y, int size) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan = 1f;

        this.size = size;
    }
}
