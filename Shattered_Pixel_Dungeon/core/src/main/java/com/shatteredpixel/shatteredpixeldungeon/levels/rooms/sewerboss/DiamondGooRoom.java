

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class DiamondGooRoom extends GooBossRoom {

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);

        Painter.fillDiamond(level, this, 1, Terrain.EMPTY);

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
            Point dir;
            if (door.x == left) {
                dir = new Point(1, 0);
            } else if (door.y == top) {
                dir = new Point(0, 1);
            } else if (door.x == right) {
                dir = new Point(-1, 0);
            } else {
                dir = new Point(0, -1);
            }

            Point curr = new Point(door);
            do {
                Painter.set(level, curr, Terrain.EMPTY_SP);
                curr.x += dir.x;
                curr.y += dir.y;
            } while (level.map[level.pointToCell(curr)] == Terrain.WALL);
        }

        Painter.fill(level, left + width() / 2 - 1, top + height() / 2 - 2, 2 + width() % 2, 4 + height() % 2, Terrain.WATER);
        Painter.fill(level, left + width() / 2 - 2, top + height() / 2 - 1, 4 + width() % 2, 2 + height() % 2, Terrain.WATER);

        setupGooNest(level);

        Goo boss = new Goo();
        boss.pos = level.pointToCell(center());
        level.mobs.add(boss);
    }

    @Override
    public boolean canPlaceWater(Point p) {
        return false;
    }
}
