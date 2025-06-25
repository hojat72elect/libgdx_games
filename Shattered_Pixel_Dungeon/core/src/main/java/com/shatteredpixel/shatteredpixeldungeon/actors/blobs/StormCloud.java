

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class StormCloud extends Blob {

    @Override
    protected void evolve() {
        super.evolve();

        int cell;

        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {
                    Dungeon.level.setCellToWater(true, cell);
                    if (fire != null) {
                        fire.clear(cell);
                    }

                    //fiery enemies take damage as if they are in toxic gas
                    Char ch = Actor.findChar(cell);
                    if (ch != null
                            && !ch.isImmune(getClass())
                            && Char.hasProp(ch, Char.Property.FIERY)) {
                        ch.damage(1 + Dungeon.scalingDepth() / 5, this);
                    }
                }
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(Speck.STORM), 0.4f);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
