

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ChasmBridgeRoom;
import com.watabou.utils.PathFinder;

public class ChasmBridgeEntranceRoom extends ChasmBridgeRoom {

    @Override
    public int minWidth() {
        return Math.max(7, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(7, super.minHeight());
    }

    @Override
    public boolean isEntrance() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int entrance;
        do {
            entrance = level.pointToCell(random(2));
        } while (spaceRect.inside(level.cellToPoint(entrance)) || level.findMob(entrance) != null);

        for (int i : PathFinder.NEIGHBOURS8) {
            Painter.set(level, entrance + i, Terrain.EMPTY);
        }

        Painter.set(level, entrance, Terrain.ENTRANCE);
        level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
    }
}
