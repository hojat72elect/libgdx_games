

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoPatchRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class RegionDecoPatchExitRoom extends RegionDecoPatchRoom {

    @Override
    public int minHeight() {
        return Math.max(7, super.minHeight());
    }

    @Override
    public int minWidth() {
        return Math.max(7, super.minWidth());
    }

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int exit;
        int tries = 30;
        boolean valid;
        do {
            exit = level.pointToCell(random(2));

            //need extra logic here as these rooms can spawn small and cramped in very rare cases
            if (tries-- > 0) {
                valid = level.map[exit] != Terrain.REGION_DECO && level.findMob(exit) == null;
            } else {
                valid = false;
                for (int i : PathFinder.NEIGHBOURS4) {
                    if (level.map[exit + i] != Terrain.REGION_DECO) {
                        valid = true;
                        break;
                    }
                }
                valid = valid && level.findMob(exit) == null;
            }
        } while (!valid);
        Painter.set(level, exit, Terrain.EXIT);

        for (int i : PathFinder.NEIGHBOURS8) {
            Painter.set(level, exit + i, Terrain.EMPTY);
        }

        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
