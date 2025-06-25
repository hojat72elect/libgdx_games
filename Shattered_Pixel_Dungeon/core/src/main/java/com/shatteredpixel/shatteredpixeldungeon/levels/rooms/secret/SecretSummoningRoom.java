

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrapMechanism;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.watabou.utils.Point;

public class SecretSummoningRoom extends SecretRoom {

    //minimum of 3x3 traps, max of 6x6 traps

    @Override
    public int maxWidth() {
        return 8;
    }

    @Override
    public int maxHeight() {
        return 8;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.SECRET_TRAP);

        Point center = center();
        level.drop(Generator.random(), level.pointToCell(center)).setHauntedIfCursed().type = Heap.Type.SKELETON;

        float revealedChance = TrapMechanism.revealHiddenTrapChance();
        float revealInc = 0;
        for (Point p : getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.SECRET_TRAP) {
                revealInc += revealedChance;
                if (revealInc >= 1) {
                    level.setTrap(new SummoningTrap().reveal(), cell);
                    Painter.set(level, cell, Terrain.TRAP);
                    revealInc--;
                } else {
                    level.setTrap(new SummoningTrap().hide(), cell);
                }
            }
        }

        entrance().set(Door.Type.HIDDEN);
    }
}
