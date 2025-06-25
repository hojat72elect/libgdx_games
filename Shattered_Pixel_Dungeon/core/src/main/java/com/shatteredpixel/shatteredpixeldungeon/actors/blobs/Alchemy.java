

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;

public class Alchemy extends Blob {

    protected int pos;

    @Override
    public Notes.Landmark landmark() {
        return Notes.Landmark.ALCHEMY;
    }

    @Override
    protected void evolve() {
        int cell;
        for (int i = area.top - 1; i <= area.bottom; i++) {
            for (int j = area.left - 1; j <= area.right; j++) {
                cell = j + i * Dungeon.level.width();
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];

                    volume += off[cell];
                }
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(Speck.factory(Speck.BUBBLE), 0.33f, 0);
    }
}
