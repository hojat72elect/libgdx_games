

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class SparkParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((SparkParticle) emitter.recycle(SparkParticle.class)).reset(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public static final Emitter.Factory STATIC = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((SparkParticle) emitter.recycle(SparkParticle.class)).resetStatic(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public SparkParticle() {
        super();

        size(2);

        acc.set(0, +50);
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;
        size = 5;

        left = lifespan = Random.Float(0.5f, 1.0f);

        speed.polar(-Random.Float(3.1415926f), Random.Float(20, 40));
    }

    public void resetStatic(float x, float y) {
        reset(x, y);

        left = lifespan = Random.Float(0.25f, 0.5f);

        acc.set(0, 0);
        speed.set(0, 0);
    }

    public void resetAttracting(float x, float y, Visual attracting) {
        reset(x, y);

        left = lifespan = Random.Float(0.2f, 0.35f);

        acc.set(0);
        speed.set((attracting.x + attracting.width / 2f) - x,
                (attracting.y + attracting.height / 2f) - y);
        speed.normalize().scale(DungeonTilemap.SIZE * 3f);

        //offset the particles slightly so they don't go too far outside of the cell
        this.x -= speed.x / 8f;
        this.y -= speed.y / 8f;
    }

    public void setMaxSize(float value) {
        size = value;
    }

    @Override
    public void update() {
        super.update();
        size(Random.Float(size * left / lifespan));
    }
}