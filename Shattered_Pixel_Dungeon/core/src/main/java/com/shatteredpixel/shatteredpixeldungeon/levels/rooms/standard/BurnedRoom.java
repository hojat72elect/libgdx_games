

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrapMechanism;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class BurnedRoom extends PatchRoom {

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
    protected float fill() {
        //past 8x8 each point of width/height decreases fill by 3%
        // e.g. a 14x14 burned room has a fill of 54%
        return Math.min(1f, 1.48f - (width() + height()) * 0.03f);
    }

    @Override
    protected int clustering() {
        return 2;
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
        Painter.fill(level, this, 1, Terrain.EMPTY);
        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

        setupPatch(level);

        float revealedChance = TrapMechanism.revealHiddenTrapChance();
        float revealInc = 0;
        for (int i = top + 1; i < bottom; i++) {
            for (int j = left + 1; j < right; j++) {
                if (!patch[xyToPatchCoords(j, i)])
                    continue;
                int cell = i * level.width() + j;
                int t;
                switch (Random.Int(5)) {
                    case 0:
                    default:
                        t = Terrain.EMPTY;
                        break;
                    case 1:
                        t = Terrain.EMBERS;
                        break;
                    case 2:
                        t = Terrain.TRAP;
                        level.setTrap(new BurningTrap().reveal(), cell);
                        break;
                    case 3:
                        revealInc += revealedChance;
                        if (revealInc >= 1) {
                            t = Terrain.TRAP;
                            level.setTrap(new BurningTrap().reveal(), cell);
                            revealInc--;
                        } else {
                            t = Terrain.SECRET_TRAP;
                            level.setTrap(new BurningTrap().hide(), cell);
                        }
                        break;
                    case 4:
                        t = Terrain.INACTIVE_TRAP;
                        BurningTrap trap = new BurningTrap();
                        trap.reveal().active = false;
                        level.setTrap(trap, cell);
                        break;
                }
                level.map[cell] = t;
            }
        }
    }

    @Override
    public boolean canPlaceWater(Point p) {
        return !inside(p) || !patch[xyToPatchCoords(p.x, p.y)];
    }

    @Override
    public boolean canPlaceGrass(Point p) {
        return !inside(p) || !patch[xyToPatchCoords(p.x, p.y)];
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return !inside(p) || !patch[xyToPatchCoords(p.x, p.y)];
    }
}
