

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Amok extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.AMOK;
    }

    @Override
    public void detach() {
        //if our target is an enemy, reset any enemy-to-enemy aggro involving it
        if (target.isAlive()) {
            if (target.alignment == Char.Alignment.ENEMY) {
                for (Mob m : Dungeon.level.mobs) {
                    if (m.alignment == Char.Alignment.ENEMY && m.isTargeting(target)) {
                        m.aggro(null);
                    }
                    if (target instanceof Mob && ((Mob) target).isTargeting(m)) {
                        ((Mob) target).aggro(null);
                    }
                }
            }
        }

        super.detach();
    }
}
