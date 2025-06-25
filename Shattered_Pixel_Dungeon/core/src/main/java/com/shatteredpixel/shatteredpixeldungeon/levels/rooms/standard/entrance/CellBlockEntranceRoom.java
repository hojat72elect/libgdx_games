

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CellBlockRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class CellBlockEntranceRoom extends CellBlockRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 1, 0};
    }

    @Override
    public boolean isEntrance() {
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
                    Painter.set(level, entrance, Terrain.ENTRANCE_SP);

                    level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
                    return;
                }
            }
        }
    }
}
