

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class LibraryRoom extends SpecialRoom {

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        Door entrance = entrance();

        Painter.fill(level, left + 1, top + 1, width() - 2, 1, Terrain.BOOKSHELF);
        Painter.drawInside(level, this, entrance, 1, Terrain.EMPTY_SP);

        int n = Random.NormalIntRange(1, 3);
        for (int i = 0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null);
            Item item;
            if (i == 0)
                item = Random.Int(2) == 0 ? new ScrollOfIdentify() : new ScrollOfRemoveCurse();
            else
                item = prize(level);
            level.drop(item, pos);
        }

        entrance.set(Door.Type.LOCKED);

        level.addItemToSpawn(new IronKey(Dungeon.depth));
    }

    private static Item prize(Level level) {

        Item prize = level.findPrizeItem(TrinketCatalyst.class);
        if (prize == null) {
            prize = level.findPrizeItem(Scroll.class);
            if (prize == null) {
                prize = Generator.random(Generator.Category.SCROLL);
            }
        }

        return prize;
    }
}
