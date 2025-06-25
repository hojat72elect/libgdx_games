

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MimicTooth;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class TreasuryRoom extends SpecialRoom {

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Painter.set(level, center(), Terrain.STATUE);

        Heap.Type heapType = Random.Int(2) == 0 ? Heap.Type.CHEST : Heap.Type.HEAP;

        int n = Random.IntRange(2, 3);
        float mimicChance = 1 / 5f * MimicTooth.mimicChanceMultiplier();
        for (int i = 0; i < n; i++) {
            Item item = level.findPrizeItem(TrinketCatalyst.class);
            if (item == null) item = new Gold().random();

            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY || level.heaps.get(pos) != null || level.findMob(pos) != null);
            if (heapType == Heap.Type.CHEST && Dungeon.depth > 1 && Random.Float() < mimicChance) {
                level.mobs.add(Mimic.spawnAt(pos, item));
            } else {
                level.drop(item, pos).type = heapType;
            }
        }

        if (heapType == Heap.Type.HEAP) {
            for (int i = 0; i < 6; i++) {
                int pos;
                do {
                    pos = level.pointToCell(random());
                } while (level.map[pos] != Terrain.EMPTY);
                level.drop(new Gold(Random.IntRange(5, 12)), pos);
            }
        }

        entrance().set(Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey(Dungeon.depth));
    }
}
