

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RingRoom;
import com.watabou.utils.Point;

public class RingExitRoom extends RingRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 1, 0};
    }

    @Override
    public boolean isExit() {
        return true;
    }

    protected int centerDecoTiles() {
        return Terrain.EMPTY_SP;
    }

    @Override
    protected void placeCenterDetail(Level level, int pos) {
        Painter.set(level, pos, Terrain.EXIT);
        level.transitions.add(new LevelTransition(level, pos, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
