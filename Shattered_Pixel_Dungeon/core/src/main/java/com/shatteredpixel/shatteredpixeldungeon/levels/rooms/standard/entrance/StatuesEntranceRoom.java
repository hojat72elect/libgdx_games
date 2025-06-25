

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StatuesRoom;
import com.watabou.utils.PathFinder;

public class StatuesEntranceRoom extends StatuesRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{3, 1, 0};
    }

    @Override
    public boolean isEntrance() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int entrance = level.pointToCell(center());

        if (width() <= 10 && height() <= 10) {
            Painter.fill(level, this, 3, Terrain.EMPTY_SP);
        }

        for (int i : PathFinder.NEIGHBOURS8) {
            if (level.map[entrance + i] != Terrain.STATUE_SP) {
                Painter.set(level, entrance + i, Terrain.EMPTY_SP);
            }
        }

        Painter.set(level, entrance, Terrain.ENTRANCE_SP);
        level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
    }
}
