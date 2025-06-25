

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class FlowParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((FlowParticle) emitter.recycle(FlowParticle.class)).reset(x, y);
        }
    };

    public FlowParticle() {
        super();

        lifespan = 0.6f;
        acc.set(0, 32);
        angularSpeed = Random.Float(-360, +360);
    }

    public void reset(float x, float y) {
        revive();

        left = lifespan;

        this.x = x;
        this.y = y;

        am = 0;
        size(0);
        speed.set(0);
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        am = (p < 0.5f ? p : 1 - p) * 0.6f;
        size((1 - p) * 4);
    }

    public static class Flow extends Emitter {

        private final int pos;

        public Flow(int pos) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileToWorld(pos);
            pos(p.x, p.y + DungeonTilemap.SIZE - 1, DungeonTilemap.SIZE, 0);

            pour(FACTORY, 0.05f);
        }

        @Override
        public void update() {

            if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {

                super.update();
            }
        }
    }
}