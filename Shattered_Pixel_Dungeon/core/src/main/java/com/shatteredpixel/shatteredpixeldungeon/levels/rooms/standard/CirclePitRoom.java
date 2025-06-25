

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class CirclePitRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(8, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(8, super.minHeight());
    }

    @Override
    public float[] sizeCatProbs() {
        return new float[]{4, 2, 1};
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

        //50/100% chance based on size
        if (sizeCat != SizeCategory.NORMAL && Random.Int(4 - sizeFactor()) == 0) {
            while (true) {
                //draw line from center (plus or minus one in each DIR) to side
                Point center = center();
                center.x += Random.IntRange(-1, 1);
                center.y += Random.IntRange(-1, 1);

                Point edge = new Point(center);
                switch (Random.Int(4)) {
                    case 0:
                        edge.x = left;
                        break;
                    case 1:
                        edge.y = top;
                        break;
                    case 2:
                        edge.x = right;
                        break;
                    case 3:
                        edge.y = bottom;
                        break;
                }

                boolean valid = true;
                for (Point door : connected.values()) {
                    if (door.equals(edge)) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    Painter.drawLine(level, edge, center, Terrain.REGION_DECO_ALT);
                    Painter.drawInside(level, this, edge, 1, Terrain.EMPTY_SP);
                    Painter.set(level, edge, Terrain.WALL);

                    //TODO pick a random cell to make empty_sp?
                }
                break;
            }
        }
    }
}
