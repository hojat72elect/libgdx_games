

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MimicTooth;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class SuspiciousChestRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(5, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(5, super.minHeight());
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

        Item i = level.findPrizeItem();

        if (i == null) {
            i = new Gold().random();
        }

        int center = level.pointToCell(center());

        Painter.set(level, center, Terrain.PEDESTAL);

        float mimicChance = 1 / 3f * MimicTooth.mimicChanceMultiplier();
        if (Random.Float() < mimicChance) {
            level.mobs.add(Mimic.spawnAt(center, i));
        } else {
            level.drop(i, center).type = Heap.Type.CHEST;
        }
    }
}
