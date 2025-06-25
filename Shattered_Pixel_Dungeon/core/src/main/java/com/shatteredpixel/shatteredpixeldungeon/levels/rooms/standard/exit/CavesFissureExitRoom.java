

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CavesFissureRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class CavesFissureExitRoom extends CavesFissureRoom {

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
        do {
            exit = level.pointToCell(random(2));
        } while (level.map[exit] == Terrain.CHASM
                || level.map[exit] == Terrain.EMPTY_SP
                || level.findMob(exit) != null);


        for (int i : PathFinder.NEIGHBOURS4) {
            if (level.map[exit + i] == Terrain.CHASM) {
                Painter.set(level, exit + i, Terrain.EMPTY);
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
