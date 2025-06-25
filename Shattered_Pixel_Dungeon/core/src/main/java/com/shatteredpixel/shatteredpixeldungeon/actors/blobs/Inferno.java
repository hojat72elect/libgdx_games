

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public class Inferno extends Blob {

    @Override
    protected void evolve() {
        super.evolve();

        int cell;
        boolean observe = false;

        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        Freezing freeze = (Freezing) Dungeon.level.blobs.get(Freezing.class);

        Blizzard bliz = (Blizzard) Dungeon.level.blobs.get(Blizzard.class);

        for (int i = area.left - 1; i <= area.right; i++) {
            for (int j = area.top - 1; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {

                    if (fire != null) fire.clear(cell);
                    if (freeze != null) freeze.clear(cell);

                    if (bliz != null && bliz.volume > 0 && bliz.cur[cell] > 0) {
                        bliz.clear(cell);
                        off[cell] = cur[cell] = 0;
                        continue;
                    }

                    Fire.burn(cell);

                    if (Dungeon.level.flamable[cell]) {
                        Dungeon.level.destroy(cell);

                        observe = true;
                        GameScene.updateMap(cell);
                    }
                } else if (Dungeon.level.flamable[cell]
                        && (cur[cell - 1] > 0
                        || cur[cell + 1] > 0
                        || cur[cell - Dungeon.level.width()] > 0
                        || cur[cell + Dungeon.level.width()] > 0)) {

                    if (fire == null || fire.cur == null || fire.cur[cell] == 0) {
                        GameScene.add(Fire.seed(cell, 4, Fire.class));
                    }
                }
            }
        }

        if (observe) {
            Dungeon.observe();
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);

        emitter.pour(Speck.factory(Speck.INFERNO, true), 0.4f);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
