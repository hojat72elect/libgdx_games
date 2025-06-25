

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

//tunnels along the rooms center, with straight lines
public class TunnelRoom extends ConnectionRoom {

    public void paint(Level level) {

        int floor = level.tunnelTile();

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

            Painter.drawLine(level, start, mid, floor);
            Painter.drawLine(level, mid, end, floor);
        }

        //fill in an extra diagonal tile at center randomly if we're a larger room with many connections
        //this makes the shape a bit more varied in these cases
        if (width() >= 7 && height() >= 7 && connected.size() > 4 && c.square() == 0) {
            Point p = new Point(c.left, c.top);
            p.x += Random.Int(2) == 0 ? 1 : -1;
            p.y += Random.Int(2) == 0 ? 1 : -1;
            //also prevent filling a tile outside the room in rare cases
            p.x = (int) GameMath.gate(left + 1, p.x, right - 1);
            p.y = (int) GameMath.gate(top + 1, p.y, bottom - 1);
            Painter.set(level, p, floor);
        }

        for (Door door : connected.values()) {
            door.set(Door.Type.TUNNEL);
        }
    }

    //returns the space which all doors must connect to (usually 1 cell, but can be more)
    //Note that, like rooms, this space is inclusive to its right and bottom sides
    protected Rect getConnectionSpace() {
        Point c = getDoorCenter();

        return new Rect(c.x, c.y, c.x, c.y);
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
        c.x = (int) GameMath.gate(left + 1, c.x, right - 1);
        c.y = (int) GameMath.gate(top + 1, c.y, bottom - 1);

        return c;
    }
}
