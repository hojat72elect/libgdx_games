

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Cripple extends FlavourBuff {

    public static final float DURATION = 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.CRIPPLE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
