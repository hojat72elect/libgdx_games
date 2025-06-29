

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class RunestoneRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 6;
    }

    @Override
    public int minHeight() {
        return 6;
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.CHASM);

        Painter.drawInside(level, this, entrance(), 2, Terrain.EMPTY_SP);
        Painter.fill(level, this, 2, Terrain.EMPTY);

        int n = Random.NormalIntRange(2, 3);
        int dropPos;
        for (int i = 0; i < n; i++) {
            do {
                dropPos = level.pointToCell(random());
            } while (level.map[dropPos] != Terrain.EMPTY || level.heaps.get(dropPos) != null);
            level.drop(prize(level), dropPos);
        }

        entrance().set(Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey(Dungeon.depth));
    }

    private static Item prize(Level level) {

        Item prize = level.findPrizeItem(TrinketCatalyst.class);
        if (prize == null) {
            prize = level.findPrizeItem(Runestone.class);
            if (prize == null) {
                prize = Generator.random(Generator.Category.STONE);
            }
        }

        return prize;
    }
}
