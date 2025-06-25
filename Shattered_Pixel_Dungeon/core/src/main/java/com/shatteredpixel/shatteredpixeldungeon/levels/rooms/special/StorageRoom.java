

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class StorageRoom extends SpecialRoom {

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        boolean honeyPot = Random.Int(2) == 0;

        int n = Random.IntRange(3, 4);
        for (int i = 0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null);
            if (honeyPot) {
                level.drop(new Honeypot(), pos);
                honeyPot = false;
            } else {
                level.drop(prize(level), pos);
            }
        }

        entrance().set(Door.Type.BARRICADE);
        level.addItemToSpawn(new PotionOfLiquidFlame());
    }

    private static Item prize(Level level) {

        if (Random.Int(3) != 0) {
            Item prize = level.findPrizeItem();
            if (prize != null)
                return prize;
        }

        return Generator.random(Random.oneOf(
                Generator.Category.POTION,
                Generator.Category.SCROLL,
                Generator.Category.FOOD,
                Generator.Category.GOLD
        ));
    }
}
