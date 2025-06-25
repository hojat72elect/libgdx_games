

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CellBlockRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class CellBlockExitRoom extends CellBlockRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 1, 0};
    }

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        while (true) {
            Point p = random(3);

            if (level.map[level.pointToCell(p)] == Terrain.EMPTY_SP) {
                boolean valid = true;
                for (int i : PathFinder.NEIGHBOURS8) {
                    if (level.map[level.pointToCell(p) + i] == Terrain.DOOR) {
                        valid = false;
                    }
                }

                if (valid) {
                    int entrance = level.pointToCell(p);
                    Painter.set(level, entrance, Terrain.EXIT);

                    level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_EXIT));
                    return;
                }
            }
        }
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
