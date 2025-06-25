

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class Blizzard extends Blob {

    @Override
    protected void evolve() {
        super.evolve();

        int cell;

        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        Freezing freeze = (Freezing) Dungeon.level.blobs.get(Freezing.class);

        Inferno inf = (Inferno) Dungeon.level.blobs.get(Inferno.class);

        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {

                    if (fire != null) fire.clear(cell);
                    if (freeze != null) freeze.clear(cell);

                    if (inf != null && inf.volume > 0 && inf.cur[cell] > 0) {
                        inf.clear(cell);
                        off[cell] = cur[cell] = 0;
                        continue;
                    }

                    Freezing.freeze(cell);
                    Freezing.freeze(cell);
                }
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(Speck.BLIZZARD, true), 0.4f);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
