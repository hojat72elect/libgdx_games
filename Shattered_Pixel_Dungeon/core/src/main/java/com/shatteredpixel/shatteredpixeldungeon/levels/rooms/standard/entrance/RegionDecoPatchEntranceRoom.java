

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoPatchRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class RegionDecoPatchEntranceRoom extends RegionDecoPatchRoom {

    @Override
    public int minHeight() {
        return Math.max(7, super.minHeight());
    }

    @Override
    public int minWidth() {
        return Math.max(7, super.minWidth());
    }

    @Override
    public boolean isEntrance() {
        return true;
    }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        if (Dungeon.depth <= 2) {
            return false;
        } else {
            return super.canMerge(l, other, p, mergeTerrain);
        }
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        if (Dungeon.depth == 1) {
            return false;
        } else {
            return super.canPlaceTrap(p);
        }
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int entrance;
        int tries = 30;
        boolean valid;
        do {
            entrance = level.pointToCell(random(2));

            //need extra logic here as these rooms can spawn small and cramped in very rare cases
            if (tries-- > 0) {
                valid = level.map[entrance] != Terrain.REGION_DECO && level.findMob(entrance) == null;
            } else {
                valid = false;
                for (int i : PathFinder.NEIGHBOURS4) {
                    if (level.map[entrance + i] != Terrain.REGION_DECO) {
                        valid = true;
                        break;
                    }
                }
                valid = valid && level.findMob(entrance) == null;
            }
        } while (!valid);
        Painter.set(level, entrance, Terrain.ENTRANCE);

        for (int i : PathFinder.NEIGHBOURS8) {
            Painter.set(level, entrance + i, Terrain.EMPTY);
        }

        if (Dungeon.depth == 1) {
            level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.SURFACE));
        } else {
            level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        }

        EntranceRoom.placeEarlyGuidePages(level, this);
    }
}
