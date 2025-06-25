

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//for pre-v2.5.3 saves, add a conversion in v3.0 and remove entirely later
public class StoneOfDisarming extends Runestone {

    private static final int DIST = 8;

    {
        image = ItemSpriteSheet.STONE_DETECT;
    }

    @Override
    protected void activate(final int cell) {
        boolean[] FOV = new boolean[Dungeon.level.length()];
        Point c = Dungeon.level.cellToPoint(cell);
        ShadowCaster.castShadow(c.x, c.y, Dungeon.level.width(), FOV, Dungeon.level.losBlocking, DIST);

        int sX = Math.max(0, c.x - DIST);
        int eX = Math.min(Dungeon.level.width() - 1, c.x + DIST);

        int sY = Math.max(0, c.y - DIST);
        int eY = Math.min(Dungeon.level.height() - 1, c.y + DIST);

        ArrayList<Trap> disarmCandidates = new ArrayList<>();

        for (int y = sY; y <= eY; y++) {
            int curr = y * Dungeon.level.width() + sX;
            for (int x = sX; x <= eX; x++) {

                if (FOV[curr]) {

                    Trap t = Dungeon.level.traps.get(curr);
                    if (t != null && t.active) {
                        disarmCandidates.add(t);
                    }
                }
                curr++;
            }
        }

        Collections.shuffle(disarmCandidates);
        Collections.sort(disarmCandidates, new Comparator<Trap>() {
            @Override
            public int compare(Trap o1, Trap o2) {
                float diff = Dungeon.level.trueDistance(cell, o1.pos) - Dungeon.level.trueDistance(cell, o2.pos);
                if (diff < 0) {
                    return -1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        //disarms at most nine traps
        while (disarmCandidates.size() > 9) {
            disarmCandidates.remove(9);
        }

        for (Trap t : disarmCandidates) {
            t.reveal();
            t.disarm();
            CellEmitter.get(t.pos).burst(Speck.factory(Speck.STEAM), 6);
        }

        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
    }
}
