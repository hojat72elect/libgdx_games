

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class CircleBasinRoom extends PatchRoom {

    @Override
    public int minWidth() {
        return sizeCat.minDim + 1;
    }

    public int minHeight() {
        return sizeCat.minDim + 1;
    }

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 3, 1};
    }

    //cannot roll even numbers
    @Override
    public Rect resize(int w, int h) {
        super.resize(w, h);
        if (width() % 2 == 0) right--;
        if (height() % 2 == 0) bottom--;
        return this;
    }

    @Override
    protected float fill() {
        return 0.5f;
    }

    @Override
    protected int clustering() {
        return 5;
    }

    @Override
    protected boolean ensurePath() {
        return false;
    }

    @Override
    protected boolean cleanEdges() {
        return false;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);

        Painter.fillEllipse(level, this, 1, Terrain.EMPTY);

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
            if (door.x == left || door.x == right) {
                Painter.drawInside(level, this, door, width() / 2, Terrain.EMPTY);
            } else {
                Painter.drawInside(level, this, door, height() / 2, Terrain.EMPTY);
            }
        }

        Painter.fillEllipse(level, this, 3, Terrain.CHASM);

        Point start = new Point(left + width() / 2, top + 3);
        Point end = new Point(left + width() / 2, bottom - 3);
        Painter.drawLine(level, start, end, Terrain.EMPTY_SP);

        start.set(left + 3, top + height() / 2);
        end.set(right - 3, top + height() / 2);
        Painter.drawLine(level, start, end, Terrain.EMPTY_SP);

        if (width() > 11 || height() > 11) {
            Point center = center();
            Painter.fill(level, center.x - 1, center.y - 1, 3, 3, Terrain.EMPTY_SP);
            Painter.set(level, center, Terrain.WALL);
        }

        setupPatch(level);
        for (int i = top + 1; i < bottom; i++) {
            for (int j = left + 1; j < right; j++) {
                int cell = i * level.width() + j;
                if (level.map[cell] == Terrain.EMPTY && patch[xyToPatchCoords(j, i)]) {
                    level.map[cell] = Terrain.WATER;
                    if (level.map[cell - level.width()] == Terrain.WALL) {
                        level.map[cell - level.width()] = Terrain.WALL_DECO;
                    }
                }
            }
        }
    }
}
