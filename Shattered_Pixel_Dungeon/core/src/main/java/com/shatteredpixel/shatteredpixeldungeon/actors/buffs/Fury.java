

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Fury extends Buff {

    public static float LEVEL = 0.5f;

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    @Override
    public boolean act() {
        if (target.HP > target.HT * LEVEL) {
            detach();
        }

        spend(TICK);

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.FURY;
    }
}
