

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.HallwayRoom;
import com.watabou.utils.Point;

public class HallwayExitRoom extends HallwayRoom {

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int exit = -1;
        for (Point p : getPoints()) {
            if (level.map[level.pointToCell(p)] == Terrain.STATUE_SP
                    || level.map[level.pointToCell(p)] == Terrain.REGION_DECO_ALT) {
                exit = level.pointToCell(p);
                break;
            }
        }
        Painter.set(level, exit, Terrain.EXIT);
        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
