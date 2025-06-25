

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CavesFissureRoom;
import com.watabou.utils.PathFinder;

public class CavesFissureEntranceRoom extends CavesFissureRoom {

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
        do {
            entrance = level.pointToCell(random(2));
        } while (level.map[entrance] == Terrain.CHASM || level.map[entrance] == Terrain.EMPTY_SP || level.findMob(entrance) != null);


        for (int i : PathFinder.NEIGHBOURS4) {
            if (level.map[entrance + i] == Terrain.CHASM) {
                Painter.set(level, entrance + i, Terrain.EMPTY);
            }
        }

        Painter.set(level, entrance, Terrain.ENTRANCE);
        level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
    }
}
