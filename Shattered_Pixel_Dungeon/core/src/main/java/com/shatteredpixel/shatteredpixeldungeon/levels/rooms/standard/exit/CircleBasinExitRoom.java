

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CircleBasinRoom;
import com.watabou.utils.Point;

public class CircleBasinExitRoom extends CircleBasinRoom {

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

        int exit = level.pointToCell(center());
        Painter.set(level, exit, Terrain.EXIT);

        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
