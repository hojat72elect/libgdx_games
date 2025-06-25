

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.watabou.utils.PathFinder;

public class OozeTrap extends Trap {

    {
        color = GREEN;
        shape = DOTS;
    }

    @Override
    public void activate() {

        for (int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[pos + i]) {
                Splash.at(pos + i, 0x000000, 5);
                Char ch = Actor.findChar(pos + i);
                if (ch != null && !ch.flying) {
                    Buff.affect(ch, Ooze.class).set(Ooze.DURATION);
                    if (ch instanceof Mob) {
                        Buff.prolong(ch, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
                    }
                }
            }
        }
    }
}
