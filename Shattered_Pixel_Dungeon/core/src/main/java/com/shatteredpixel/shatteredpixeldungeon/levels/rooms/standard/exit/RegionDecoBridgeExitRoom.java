

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoBridgeRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class RegionDecoBridgeExitRoom extends RegionDecoBridgeRoom {

    @Override
    public int minWidth() {
        return Math.max(8, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(8, super.minHeight());
    }

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int exit;
        boolean valid;
        do {
            valid = true;
            exit = level.pointToCell(random(2));

            if (spaceRect.inside(level.cellToPoint(exit))) {
                valid = false;
            } else {
                for (int i : PathFinder.NEIGHBOURS8) {
                    if (level.map[exit + i] == Terrain.REGION_DECO_ALT) {
                        valid = false;
                        break;
                    }
                }
            }
        } while (!valid);

        Painter.set(level, exit, Terrain.EXIT);
        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
