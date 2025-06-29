

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoBridgeRoom;
import com.watabou.utils.PathFinder;

public class RegionDecoBridgeEntranceRoom extends RegionDecoBridgeRoom {

    @Override
    public int minWidth() {
        return Math.max(8, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(8, super.minHeight());
    }

    @Override
    public boolean isEntrance() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int entrance;
        boolean valid;
        do {
            valid = true;
            entrance = level.pointToCell(random(2));

            if (spaceRect.inside(level.cellToPoint(entrance))) {
                valid = false;
            } else {
                for (int i : PathFinder.NEIGHBOURS8) {
                    if (level.map[entrance + i] == Terrain.REGION_DECO_ALT) {
                        valid = false;
                        break;
                    }
                }
            }
        } while (!valid);

        Painter.set(level, entrance, Terrain.ENTRANCE);
        level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
    }
}
