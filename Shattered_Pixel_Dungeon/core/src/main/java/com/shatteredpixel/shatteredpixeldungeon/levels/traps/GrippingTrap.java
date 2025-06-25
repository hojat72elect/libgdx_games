

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;

public class GrippingTrap extends Trap {

    {
        color = GREY;
        shape = DOTS;

        disarmedByActivation = false;
        avoidsHallways = true;
    }

    @Override
    public void activate() {

        Char c = Actor.findChar(pos);

        if (c != null && !c.flying) {
            if (c instanceof Mob) {
                Buff.prolong(c, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
            }
            int damage = Math.max(0, (2 + scalingDepth() / 2) - c.drRoll() / 2);
            Buff.affect(c, Bleeding.class).set(damage);
            Buff.prolong(c, Cripple.class, Cripple.DURATION);
            Wound.hit(c);
        } else {
            Wound.hit(pos);
        }
    }
}
