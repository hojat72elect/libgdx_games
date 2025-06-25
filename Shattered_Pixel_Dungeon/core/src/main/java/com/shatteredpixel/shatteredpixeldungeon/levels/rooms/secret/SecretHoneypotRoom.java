

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class SecretHoneypotRoom extends SecretRoom {

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Point brokenPotPos = center();

        brokenPotPos.x = (brokenPotPos.x + entrance().x) / 2;
        brokenPotPos.y = (brokenPotPos.y + entrance().y) / 2;

        Honeypot.ShatteredPot pot = new Honeypot.ShatteredPot();
        level.drop(pot, level.pointToCell(brokenPotPos));

        Bee bee = new Bee();
        bee.spawn(Dungeon.depth);
        bee.HP = bee.HT;
        bee.pos = level.pointToCell(brokenPotPos);
        level.mobs.add(bee);

        bee.setPotInfo(level.pointToCell(brokenPotPos), null);

        placeItem(new Honeypot(), level);

        placeItem(new Bomb().random(), level);

        entrance().set(Door.Type.HIDDEN);
    }

    private void placeItem(Item item, Level level) {
        int itemPos;
        do {
            itemPos = level.pointToCell(random());
        } while (level.heaps.get(itemPos) != null);

        level.drop(item, itemPos);
    }
}
