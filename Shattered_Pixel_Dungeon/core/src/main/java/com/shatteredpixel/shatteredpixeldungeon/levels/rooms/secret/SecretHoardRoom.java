

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class SecretHoardRoom extends SecretRoom {

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Class<? extends Trap> trapClass;
        if (Random.Int(2) == 0) {
            trapClass = RockfallTrap.class;
        } else if (Dungeon.depth >= 10) {
            trapClass = DisintegrationTrap.class;
        } else {
            trapClass = PoisonDartTrap.class;
        }

        int goldPos;
        //half of the internal space of the room
        int totalGold = ((width() - 2) * (height() - 2)) / 2;

        //no matter how much gold it drops, roughly equals 8 gold stacks.
        float goldRatio = 8 / (float) totalGold;
        for (int i = 0; i < totalGold; i++) {
            do {
                goldPos = level.pointToCell(random());
            } while (level.heaps.get(goldPos) != null);
            Item gold = new Gold().random();
            gold.quantity(Math.round(gold.quantity() * goldRatio));
            level.drop(gold, goldPos);
        }

        for (Point p : getPoints()) {
            if (Random.Int(2) == 0 && level.map[level.pointToCell(p)] == Terrain.EMPTY) {
                level.setTrap(Reflection.newInstance(trapClass).reveal(), level.pointToCell(p));
                Painter.set(level, p, Terrain.TRAP);
            }
        }

        entrance().set(Door.Type.HIDDEN);
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }
}
