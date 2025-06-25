

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Drowsy extends FlavourBuff {

    public static final float DURATION = 5f;

    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.DROWSY;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    public boolean attachTo(Char target) {
        return !target.isImmune(Sleep.class) && super.attachTo(target);
    }

    @Override
    public boolean act() {
        Buff.affect(target, MagicalSleep.class);

        return super.act();
    }
}
