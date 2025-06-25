

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class StoneOfFlock extends Runestone {

    {
        image = ItemSpriteSheet.STONE_FLOCK;
    }

    @Override
    protected void activate(int cell) {

        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), 2);
        ArrayList<Integer> spawnPoints = new ArrayList<>();
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                spawnPoints.add(i);
            }
        }

        for (int i : spawnPoints) {
            if (Dungeon.level.insideMap(i)
                    && Actor.findChar(i) == null
                    && !(Dungeon.level.pit[i])) {
                Sheep sheep = new Sheep();
                sheep.initialize(8);
                sheep.pos = i;
                GameScene.add(sheep);
                Dungeon.level.occupyCell(sheep);
                CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 4);
            }
        }

        CellEmitter.get(cell).burst(Speck.factory(Speck.WOOL), 4);
        Sample.INSTANCE.play(Assets.Sounds.PUFF);
        Sample.INSTANCE.play(Assets.Sounds.SHEEP);
    }
}
