

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.LibraryRingRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LibraryRingExitRoom extends LibraryRingRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 13);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 13);
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

        Painter.fill(level, this, 5, Terrain.EMPTY_SP);

        Point p = center();
        Painter.set(level, p, Terrain.EXIT);
        level.transitions.add(new LevelTransition(level, level.pointToCell(p), LevelTransition.Type.REGULAR_EXIT));

        int dirX = 0, dirY = 0;
        if (Random.Int(2) == 0) {
            dirX = Random.Int(2) == 0 ? +1 : -1;
        } else {
            dirY = Random.Int(2) == 0 ? +1 : -1;
        }

        p.x += dirX;
        p.y += dirY;
        while (level.map[level.pointToCell(p)] != Terrain.EMPTY) {
            Painter.set(level, p, Terrain.EMPTY_SP);
            p.x += dirX;
            p.y += dirY;
        }
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }
}
