

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.LibraryHallRoom;
import com.watabou.utils.Point;

public class LibraryHallEntranceRoom extends LibraryHallRoom {

    @Override
    public boolean isEntrance() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        while (true) {
            Point p = random(2);

            if (level.map[level.pointToCell(p)] == Terrain.REGION_DECO) {
                int entrance = level.pointToCell(p);
                Painter.set(level, entrance, Terrain.ENTRANCE);

                level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
                return;
            }
        }
    }
}
