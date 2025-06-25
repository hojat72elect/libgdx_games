

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RingRoom;

public class RingEntranceRoom extends RingRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 1, 0};
    }

    @Override
    public boolean isEntrance() {
        return true;
    }

    protected int centerDecoTiles() {
        return Terrain.EMPTY_SP;
    }

    @Override
    protected void placeCenterDetail(Level level, int pos) {
        Painter.set(level, pos, Terrain.ENTRANCE_SP);
        level.transitions.add(new LevelTransition(level, pos, LevelTransition.Type.REGULAR_ENTRANCE));
    }
}
