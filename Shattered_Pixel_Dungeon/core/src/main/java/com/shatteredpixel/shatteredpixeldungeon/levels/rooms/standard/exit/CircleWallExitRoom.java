

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CircleWallRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class CircleWallExitRoom extends CircleWallRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 11);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 11);
    }

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 1, 0};
    }

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        Point p = center();

        int cell = level.pointToCell(p);
        for (int i : PathFinder.NEIGHBOURS8) {
            if (level.map[cell + 2 * i] == Terrain.WALL) {
                Painter.set(level, cell + i, Terrain.EMPTY);
            }
        }
        Painter.set(level, p, Terrain.EXIT);
        level.transitions.add(new LevelTransition(level, level.pointToCell(p), LevelTransition.Type.REGULAR_EXIT));

        int xDir = 0, yDir = 0;
        if (Random.Int(2) == 0) {
            xDir = Random.Int(2) == 0 ? 1 : -1;
        } else {
            yDir = Random.Int(2) == 0 ? 1 : -1;
        }

        p.x += 2 * xDir;
        p.y += 2 * yDir;

        while (level.map[level.pointToCell(p)] == Terrain.WALL) {
            Painter.set(level, p, Terrain.EMPTY);
            p.x += xDir;
            p.y += yDir;
        }
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
