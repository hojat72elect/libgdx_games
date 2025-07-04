

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class HallwayRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(5, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(5, super.minHeight());
    }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return other instanceof HallwayRoom && super.canMerge(l, other, p, mergeTerrain);
    }

    @Override
    public void merge(Level l, Room other, Rect merge, int mergeTerrain) {
        super.merge(l, other, merge, mergeTerrain);
        Painter.set(l, connected.get(other), Terrain.EMPTY_SP);
    }

    //FIXME lots of copy-pasta from tunnel rooms here
    @Override
    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Rect c = getConnectionSpace();

        for (Door door : connected.values()) {

            Point start;
            Point mid;
            Point end;

            start = new Point(door);
            if (start.x == left) start.x++;
            else if (start.y == top) start.y++;
            else if (start.x == right) start.x--;
            else if (start.y == bottom) start.y--;

            int rightShift;
            int downShift;

            if (start.x < c.left) rightShift = c.left - start.x;
            else if (start.x > c.right) rightShift = c.right - start.x;
            else rightShift = 0;

            if (start.y < c.top) downShift = c.top - start.y;
            else if (start.y > c.bottom) downShift = c.bottom - start.y;
            else downShift = 0;

            //always goes inward first
            if (door.x == left || door.x == right) {
                mid = new Point(start.x + rightShift, start.y);
                end = new Point(mid.x, mid.y + downShift);
            } else {
                mid = new Point(start.x, start.y + downShift);
                end = new Point(mid.x + rightShift, mid.y);
            }

            Painter.drawLine(level, start, mid, Terrain.EMPTY_SP);
            Painter.drawLine(level, mid, end, Terrain.EMPTY_SP);
        }

        Painter.fill(level, c.left, c.top, 3, 3, Terrain.EMPTY_SP);
        if (Random.Int(2) == 0) {
            Painter.fill(level, c.left + 1, c.top + 1, 1, 1, Terrain.STATUE_SP);
        } else {
            Painter.fill(level, c.left + 1, c.top + 1, 1, 1, Terrain.REGION_DECO_ALT);
        }

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }
    }

    //returns the space which all doors must connect to (usually 1 cell, but can be more)
    //Note that, like rooms, this space is inclusive to its right and bottom sides
    protected Rect getConnectionSpace() {
        Point c = center();

        c.x = (int) GameMath.gate(left + 2, c.x, right - 2);
        c.y = (int) GameMath.gate(top + 2, c.y, bottom - 2);

        return new Rect(c.x - 1, c.y - 1, c.x + 1, c.y + 1);
    }

    //returns a point equidistant from all doors this room has
    protected final Point getDoorCenter() {
        PointF doorCenter = new PointF(0, 0);

        for (Door door : connected.values()) {
            doorCenter.x += door.x;
            doorCenter.y += door.y;
        }

        Point c = new Point((int) doorCenter.x / connected.size(), (int) doorCenter.y / connected.size());
        if (Random.Float() < doorCenter.x % 1) c.x++;
        if (Random.Float() < doorCenter.y % 1) c.y++;
        c.x = (int) GameMath.gate(left + 2, c.x, right - 2);
        c.y = (int) GameMath.gate(top + 2, c.y, bottom - 2);

        return c;
    }
}
