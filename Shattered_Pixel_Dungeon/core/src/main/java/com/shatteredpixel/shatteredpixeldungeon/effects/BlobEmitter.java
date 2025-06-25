

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;
import com.watabou.utils.RectF;

public class BlobEmitter extends Emitter {

    private final Blob blob;

    public BlobEmitter(Blob blob) {

        super();

        this.blob = blob;
        blob.use(this);
    }

    public RectF bound = new RectF(0, 0, 1, 1);

    @Override
    protected void emit(int index) {

        if (blob.volume <= 0) {
            return;
        }

        if (blob.area.isEmpty())
            blob.setupArea();

        int[] map = blob.cur;
        float size = DungeonTilemap.SIZE;

        int cell;
        for (int i = blob.area.left; i < blob.area.right; i++) {
            for (int j = blob.area.top; j < blob.area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cell < Dungeon.level.heroFOV.length
                        && (Dungeon.level.heroFOV[cell] || blob.alwaysVisible)
                        && map[cell] > 0) {
                    float x = (i + Random.Float(bound.left, bound.right)) * size;
                    float y = (j + Random.Float(bound.top, bound.bottom)) * size;
                    factory.emit(this, index, x, y);
                }
            }
        }
    }
}
