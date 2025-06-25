

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.PillarsRoom;
import com.watabou.utils.PathFinder;

public class PillarsEntranceRoom extends PillarsRoom {

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

        int entrance;
        boolean valid;
        do {
            entrance = level.pointToCell(random(2));
            valid = true;

            for (int i : PathFinder.NEIGHBOURS4) {
                if (i == -level.width()) continue;
                if (level.map[entrance + i] == Terrain.WALL) {
                    valid = false;
                }
            }
        } while (level.findMob(entrance) != null || level.map[entrance] == Terrain.WALL || !valid);
        Painter.set(level, entrance, Terrain.ENTRANCE);

        level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
    }
}
