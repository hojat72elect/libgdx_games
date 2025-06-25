

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class StripedRoom extends StandardRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{2, 1, 0};
    }

    @Override
    public void merge(Level l, Room other, Rect merge, int mergeTerrain) {
        if (other instanceof StripedRoom && mergeTerrain == Terrain.EMPTY) {
            super.merge(l, other, merge, Terrain.EMPTY_SP);
        } else {
            super.merge(l, other, merge, mergeTerrain);
        }
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

        if (sizeCat == SizeCategory.NORMAL) {
            Painter.fill(level, this, 1, Terrain.EMPTY_SP);
            if (width() > height() || (width() == height() && Random.Int(2) == 0)) {
                for (int i = left + 2; i < right; i += 2) {
                    Painter.fill(level, i, top + 1, 1, height() - 2, Terrain.HIGH_GRASS);
                }
            } else {
                for (int i = top + 2; i < bottom; i += 2) {
                    Painter.fill(level, left + 1, i, width() - 2, 1, Terrain.HIGH_GRASS);
                }
            }
        } else if (sizeCat == SizeCategory.LARGE) {
            int layers = (Math.min(width(), height()) - 1) / 2;
            for (int i = 1; i <= layers; i++) {
                Painter.fill(level, this, i, (i % 2 == 1) ? Terrain.EMPTY_SP : Terrain.HIGH_GRASS);
            }
        }
    }
}
