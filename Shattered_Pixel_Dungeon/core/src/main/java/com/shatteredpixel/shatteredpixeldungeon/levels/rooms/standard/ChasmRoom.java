

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Rect;

public class ChasmRoom extends PatchRoom {

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
    public void merge(Level l, Room other, Rect merge, int mergeTerrain) {
        if (mergeTerrain == Terrain.EMPTY
                && (other instanceof ChasmRoom || other instanceof PlatformRoom)) {
            super.merge(l, other, merge, Terrain.CHASM);
            Painter.set(l, connected.get(other), Terrain.EMPTY);
        } else {
            super.merge(l, other, merge, mergeTerrain);
        }
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);
        for (Room.Door door : connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        setupPatch(level);

        fillPatch(level, Terrain.CHASM);
    }
}
