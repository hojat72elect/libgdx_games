

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

public class CaveRoom extends PatchRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{4, 2, 1};
    }

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
        //fill scales from ~30% at 4x4, to ~60% at 18x18
        // normal   ~30% to ~40%
        // large    ~40% to ~50%
        // giant    ~50% to ~60%
        int scale = Math.min(width() * height(), 18 * 18);
        return 0.30f + scale / 1024f;
    }

    @Override
    protected int clustering() {
        return 3;
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
        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

        setupPatch(level);
        fillPatch(level, Terrain.WALL);
    }
}
