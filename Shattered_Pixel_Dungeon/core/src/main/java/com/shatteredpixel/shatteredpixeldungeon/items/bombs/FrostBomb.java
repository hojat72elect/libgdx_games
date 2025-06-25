

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

public class FrostBomb extends Bomb {

    {
        image = ItemSpriteSheet.FROST_BOMB;
    }

    @Override
    protected int explosionRange() {
        return 2;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);
        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), explosionRange());
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                GameScene.add(Blob.seed(i, 10, Freezing.class));
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    Buff.affect(ch, Frost.class, 2f);
                }
            }
        }
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 30);
    }
}
