

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.LibraryHallRoom;
import com.watabou.utils.Point;

public class LibraryHallExitRoom extends LibraryHallRoom {

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        while (true) {
            Point p = random(2);

            if (level.map[level.pointToCell(p)] == Terrain.REGION_DECO) {
                int exit = level.pointToCell(p);
                Painter.set(level, exit, Terrain.EXIT);

                level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
                return;
            }
        }
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
