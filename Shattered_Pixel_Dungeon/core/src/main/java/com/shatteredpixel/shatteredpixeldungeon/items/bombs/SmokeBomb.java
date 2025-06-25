

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

public class SmokeBomb extends Bomb {

    {
        image = ItemSpriteSheet.SMOKE_BOMB;
    }

    @Override
    protected int explosionRange() {
        return 2;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        int centerVolume = 1000; //40*25
        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), explosionRange());
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                GameScene.add(Blob.seed(i, 40, SmokeScreen.class));
                centerVolume -= 40;
            }
        }

        //excess volume if some cells were blocked
        if (centerVolume > 0) {
            GameScene.add(Blob.seed(cell, centerVolume, SmokeScreen.class));
        }
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 40);
    }
}
