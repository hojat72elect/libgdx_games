

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.PillarsRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class PillarsExitRoom extends PillarsRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{3, 1, 0};
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
            exit = level.pointToCell(random(2));
            valid = true;

            for (int i : PathFinder.NEIGHBOURS4) {
                if (i == -level.width()) continue;
                if (level.map[exit + i] == Terrain.WALL) {
                    valid = false;
                }
            }
        } while (level.findMob(exit) != null || level.map[exit] == Terrain.WALL || !valid);
        Painter.set(level, exit, Terrain.EXIT);

        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
