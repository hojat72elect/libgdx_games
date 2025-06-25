

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class GrassyGraveRoom extends StandardRoom {

    @Override
    public void merge(Level l, Room other, Rect merge, int mergeTerrain) {
        if (mergeTerrain == Terrain.EMPTY &&
                (other instanceof GrassyGraveRoom || other instanceof PlantsRoom)) {
            super.merge(l, other, merge, Terrain.GRASS);
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

        Painter.fill(level, this, 1, Terrain.GRASS);

        int w = width() - 2;
        int h = height() - 2;
        int nGraves = Math.max(w, h) / 2;

        int index = Random.Int(nGraves);

        int shift = Random.Int(2);
        for (int i = 0; i < nGraves; i++) {
            int pos = w > h ?
                    left + 1 + shift + i * 2 + (top + 2 + Random.Int(h - 2)) * level.width() :
                    (left + 2 + Random.Int(w - 2)) + (top + 1 + shift + i * 2) * level.width();
            level.drop(i == index ? Generator.random() : new Gold().random(), pos).type = Heap.Type.TOMB;
        }
    }
}
