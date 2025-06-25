

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrapMechanism;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class MinefieldRoom extends StandardRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{4, 1, 0};
    }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        int cell = l.pointToCell(pointInside(p, 1));
        return l.map[cell] == Terrain.EMPTY;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);
        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

        int mines = (int) Math.round(Math.sqrt(square()));

        switch (sizeCat) {
            case NORMAL:
                mines -= 3;
                break;
            case LARGE:
                mines += 3;
                break;
            case GIANT:
                mines += 9;
                break;
        }

        float revealedChance = TrapMechanism.revealHiddenTrapChance();
        float revealInc = 0;
        for (int i = 0; i < mines; i++) {
            int pos;
            do {
                pos = level.pointToCell(random(1));
            } while (level.traps.get(pos) != null);

            //randomly places some embers around the mines
            for (int j = 0; j < 8; j++) {
                int c = PathFinder.NEIGHBOURS8[Random.Int(8)];
                if (level.traps.get(pos + c) == null && level.map[pos + c] == Terrain.EMPTY) {
                    Painter.set(level, pos + c, Terrain.EMBERS);
                }
            }

            revealInc += revealedChance;
            if (revealInc >= 1) {
                Painter.set(level, pos, Terrain.TRAP);
                level.setTrap(new ExplosiveTrap().reveal(), pos);
                revealInc--;
            } else {
                Painter.set(level, pos, Terrain.SECRET_TRAP);
                level.setTrap(new ExplosiveTrap().hide(), pos);
            }
        }
    }
}
