

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class WeakFloorRoom extends SpecialRoom {

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.CHASM);

        Door door = entrance();
        door.set(Door.Type.REGULAR);

        Point well = null;

        if (door.x == left) {
            for (int i = top + 1; i < bottom; i++) {
                Painter.drawInside(level, this, new Point(left, i), Random.IntRange(1, width() - 4), Terrain.EMPTY_SP);
            }
            well = new Point(right - 1, Random.Int(2) == 0 ? top + 2 : bottom - 1);
        } else if (door.x == right) {
            for (int i = top + 1; i < bottom; i++) {
                Painter.drawInside(level, this, new Point(right, i), Random.IntRange(1, width() - 4), Terrain.EMPTY_SP);
            }
            well = new Point(left + 1, Random.Int(2) == 0 ? top + 2 : bottom - 1);
        } else if (door.y == top) {
            for (int i = left + 1; i < right; i++) {
                Painter.drawInside(level, this, new Point(i, top), Random.IntRange(1, height() - 4), Terrain.EMPTY_SP);
            }
            well = new Point(Random.Int(2) == 0 ? left + 1 : right - 1, bottom - 1);
        } else if (door.y == bottom) {
            for (int i = left + 1; i < right; i++) {
                Painter.drawInside(level, this, new Point(i, bottom), Random.IntRange(1, height() - 4), Terrain.EMPTY_SP);
            }
            well = new Point(Random.Int(2) == 0 ? left + 1 : right - 1, top + 2);
        }

        Painter.set(level, well, Terrain.CHASM);
        CustomTilemap vis = new HiddenWell();
        vis.pos(well.x, well.y);
        level.customTiles.add(vis);

        Blob.seed(well.x + level.width() * well.y, 1, WellID.class, level);
    }

    public static class HiddenWell extends CustomTilemap {

        {
            texture = Assets.Environment.WEAK_FLOOR;
            tileW = tileH = 1;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(new int[]{Dungeon.depth / 5}, 1);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }

    //we use a blob to track visibility of the well, yes this sucks
    public static class WellID extends Blob {

        @Override
        public Notes.Landmark landmark() {
            return Notes.Landmark.DISTANT_WELL;
        }

        @Override
        protected void evolve() {
            int cell;
            for (int i = area.top - 1; i <= area.bottom; i++) {
                for (int j = area.left - 1; j <= area.right; j++) {
                    cell = j + i * Dungeon.level.width();
                    if (Dungeon.level.insideMap(cell)) {
                        off[cell] = cur[cell];

                        volume += off[cell];
                    }
                }
            }
        }
    }
}
