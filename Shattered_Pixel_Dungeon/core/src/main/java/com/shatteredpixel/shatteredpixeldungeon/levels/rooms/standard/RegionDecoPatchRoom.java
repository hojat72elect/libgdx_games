

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;

public class RegionDecoPatchRoom extends PatchRoom {

    @Override
    public int minHeight() {
        return Math.max(5, super.minHeight());
    }

    @Override
    public int minWidth() {
        return Math.max(5, super.minWidth());
    }

    @Override
    protected float fill() {
        //fill scales from ~20% at 4x4, to ~30% at 10x10
        int scale = Math.min(width() * height(), 10 * 10);
        return 0.20f + scale / 1024f;
    }

    @Override
    protected int clustering() {
        return 1;
    }

    @Override
    protected boolean ensurePath() {
        return connected.size() > 0;
    }

    @Override
    protected boolean cleanEdges() {
        return true;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);
        for (Room.Door door : connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        setupPatch(level);
        fillPatch(level, Terrain.REGION_DECO);
    }
}
