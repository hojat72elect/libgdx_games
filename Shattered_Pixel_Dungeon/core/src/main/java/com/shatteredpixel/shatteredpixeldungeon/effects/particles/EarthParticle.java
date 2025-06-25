

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class EarthParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((EarthParticle) emitter.recycle(EarthParticle.class)).reset(x, y);
        }
    };

    public static final Emitter.Factory SMALL = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((EarthParticle) emitter.recycle(EarthParticle.class)).resetSmall(x, y);
        }
    };

    public static final Emitter.Factory FALLING = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((EarthParticle) emitter.recycle(EarthParticle.class)).resetFalling(x, y);
        }
    };

    public EarthParticle() {
        super();

        color(ColorMath.random(0x444444, 0x777766));
        angle = Random.Float(-30, 30);
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan = 0.5f;
        size = 16;

        acc.y = 0;
        speed.y = 0;
        angularSpeed = 0;
    }

    public void resetSmall(float x, float y) {
        reset(x, y);

        left = lifespan = 1f;
        size = 8;
    }

    public void resetFalling(float x, float y) {
        reset(x, y);

        left = lifespan = 1f;
        size = 8;

        acc.y = 30;
        speed.y = -5;
        angularSpeed = Random.Float(-90, 90);
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        size((p < 0.5f ? p : 1 - p) * size);
    }
}