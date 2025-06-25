

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Roots extends FlavourBuff {

    public static final float DURATION = 5f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (!target.flying && super.attachTo(target)) {
            target.rooted = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.rooted = false;
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.ROOTS;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
