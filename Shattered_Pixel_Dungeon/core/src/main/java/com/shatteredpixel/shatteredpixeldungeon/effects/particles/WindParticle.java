

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WindParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((WindParticle) emitter.recycle(WindParticle.class)).reset(x, y);
        }
    };

    private static float angle = Random.Float(PointF.PI2);
    private static PointF speed = new PointF().polar(angle, 5);


    public WindParticle() {
        super();

        lifespan = Random.Float(1, 2);
        scale.set(size = Random.Float(3));
    }

    public void reset(float x, float y) {
        revive();

        left = lifespan;

        super.speed.set(WindParticle.speed);
        super.speed.scale(size);

        this.x = x - super.speed.x * lifespan / 2;
        this.y = y - super.speed.y * lifespan / 2;

        angle += Random.Float(-0.1f, +0.1f);
        speed = new PointF().polar(angle, 5);

        am = 0;
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        am = (p < 0.5f ? p : 1 - p) * size * 0.2f;
    }

    public static class Wind extends Emitter {

        private final int pos;

        public Wind(int pos) {
            super();

            this.pos = pos;
            PointF p = DungeonTilemap.tileToWorld(pos);
            pos(p.x, p.y, DungeonTilemap.SIZE, DungeonTilemap.SIZE);

            pour(FACTORY, 2.5f);
        }

        @Override
        public void update() {

            if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {

                super.update();
            }
        }
    }
}