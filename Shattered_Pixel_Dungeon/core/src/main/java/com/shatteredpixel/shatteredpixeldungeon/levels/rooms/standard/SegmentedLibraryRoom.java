

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

//FIXME some copypasta from segmented room with changed constants in here, might want to externalize
public class SegmentedLibraryRoom extends StandardRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 3, 1};
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.BOOKSHELF);
        Painter.fill(level, this, 2, Terrain.EMPTY_SP);

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
            //set door areas to be empty to help with create walls logic
            Painter.drawInside(level, this, door, 2, Terrain.EMPTY_SP);
        }

        createWalls(level, new Rect(left + 2, top + 2, right - 2, bottom - 2));
    }

    private void createWalls(Level level, Rect area) {
        if (Math.max(area.width() + 1, area.height() + 1) < 4
                || Math.min(area.width() + 1, area.height() + 1) < 3) {
            return;
        }

        int tries = 10;

        //splitting top/bottom
        if (area.width() > area.height() || (area.width() == area.height() && Random.Int(2) == 0)) {

            do {
                int splitX = Random.IntRange(area.left + 2, area.right - 2);

                if (level.map[splitX + level.width() * (area.top - 1)] == Terrain.BOOKSHELF
                        && level.map[splitX + level.width() * (area.bottom + 1)] == Terrain.BOOKSHELF) {
                    tries = 0;

                    Painter.drawLine(level, new Point(splitX, area.top), new Point(splitX, area.bottom), Terrain.BOOKSHELF);

                    int spaceTop = Random.IntRange(area.top, area.bottom - 1);
                    Painter.set(level, splitX, spaceTop, Terrain.EMPTY_SP);
                    //Painter.set(level, splitX, spaceTop+1, Terrain.EMPTY);

                    createWalls(level, new Rect(area.left, area.top, splitX - 1, area.bottom));
                    createWalls(level, new Rect(splitX + 1, area.top, area.right, area.bottom));
                }
            } while (--tries > 0);

            //splitting left/right
        } else {

            do {
                int splitY = Random.IntRange(area.top + 2, area.bottom - 2);

                if (level.map[area.left - 1 + level.width() * splitY] == Terrain.BOOKSHELF
                        && level.map[area.right + 1 + level.width() * splitY] == Terrain.BOOKSHELF) {
                    tries = 0;

                    Painter.drawLine(level, new Point(area.left, splitY), new Point(area.right, splitY), Terrain.BOOKSHELF);

                    int spaceLeft = Random.IntRange(area.left, area.right - 1);
                    Painter.set(level, spaceLeft, splitY, Terrain.EMPTY_SP);
                    //Painter.set(level, spaceLeft+1, splitY, Terrain.EMPTY);

                    createWalls(level, new Rect(area.left, area.top, area.right, splitY - 1));
                    createWalls(level, new Rect(area.left, splitY + 1, area.right, area.bottom));
                }
            } while (--tries > 0);
        }
    }
}
