

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.watabou.utils.PathFinder;

public class ExplosiveTrap extends Trap {

    {
        color = ORANGE;
        shape = DIAMOND;
    }

    @Override
    public void activate() {

        for (int i : PathFinder.NEIGHBOURS9) {
            if (Actor.findChar(pos + i) instanceof Mob) {
                Buff.prolong(Actor.findChar(pos + i), Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
        }

        new Bomb().explode(pos);
        if (reclaimed && !Dungeon.hero.isAlive()) {
            Badges.validateDeathFromFriendlyMagic();
        }
    }
}
