

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class LibraryRingRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 9);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 9);
    }

    @Override
    public float[] sizeCatProbs() {
        return new float[]{4, 2, 1};
    }

    //cannot roll odd numbers if it is giant
    @Override
    public Rect resize(int w, int h) {
        super.resize(w, h);
        if (sizeCat == SizeCategory.GIANT) {
            if (width() % 2 == 1) right--;
            if (height() % 2 == 1) bottom--;
        }
        return this;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.BOOKSHELF);
        Painter.fill(level, this, 2, Terrain.EMPTY);

        Painter.fill(level, this, 4, Terrain.BOOKSHELF);

        if (sizeCat == SizeCategory.GIANT) {
            Point c = new Point((left + right) / 2, (top + bottom) / 2); //always round down
            Painter.fill(level, c.x - 4, c.y, 10, 2, Terrain.EMPTY);
            Painter.fill(level, c.x, c.y - 4, 2, 10, Terrain.EMPTY);
        }

        for (Room.Door door : connected.values()) {
            door.set(Room.Door.Type.REGULAR);
            Painter.drawInside(level, this, door, 2, Terrain.EMPTY);
        }
    }
}
