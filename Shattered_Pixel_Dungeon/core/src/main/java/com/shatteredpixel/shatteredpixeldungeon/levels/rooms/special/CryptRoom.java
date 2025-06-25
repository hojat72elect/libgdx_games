

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class CryptRoom extends SpecialRoom {

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Point c = center();
        int cx = c.x;
        int cy = c.y;

        Door entrance = entrance();

        entrance.set(Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey(Dungeon.depth));

        if (entrance.x == left) {
            Painter.set(level, new Point(right - 1, top + 1), Terrain.STATUE);
            Painter.set(level, new Point(right - 1, bottom - 1), Terrain.STATUE);
            cx = right - 2;
        } else if (entrance.x == right) {
            Painter.set(level, new Point(left + 1, top + 1), Terrain.STATUE);
            Painter.set(level, new Point(left + 1, bottom - 1), Terrain.STATUE);
            cx = left + 2;
        } else if (entrance.y == top) {
            Painter.set(level, new Point(left + 1, bottom - 1), Terrain.STATUE);
            Painter.set(level, new Point(right - 1, bottom - 1), Terrain.STATUE);
            cy = bottom - 2;
        } else if (entrance.y == bottom) {
            Painter.set(level, new Point(left + 1, top + 1), Terrain.STATUE);
            Painter.set(level, new Point(right - 1, top + 1), Terrain.STATUE);
            cy = top + 2;
        }

        level.drop(prize(level), cx + cy * level.width()).type = Heap.Type.TOMB;
    }

    private static Item prize(Level level) {

        //1 floor set higher than normal
        Armor prize = Generator.randomArmor((Dungeon.depth / 5) + 1);

        if (Challenges.isItemBlocked(prize)) {
            return new Gold().random();
        }

        //always generate the curse to prevent parchment scrap from altering levelgen
        Armor.Glyph curse = Armor.Glyph.randomCurse();

        //if it isn't already cursed, give it a free upgrade
        if (!prize.cursed) {
            prize.upgrade();
            //curse the armor, unless it has a glyph
            if (!prize.hasGoodGlyph()) {
                prize.inscribe(curse);
            }
        }
        prize.cursed = prize.cursedKnown = true;

        return prize;
    }
}
