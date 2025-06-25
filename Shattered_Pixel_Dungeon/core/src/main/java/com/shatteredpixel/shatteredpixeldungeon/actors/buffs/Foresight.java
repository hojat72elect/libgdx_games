

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Foresight extends FlavourBuff {

    public static final float DURATION = 400f;

    public static final int DISTANCE = 8;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.FORESIGHT;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            //this way we get a nice VFX sweep on initial activation
            if (target == Dungeon.hero) {
                Dungeon.level.mapped[target.pos] = false;
                Dungeon.hero.search(false);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
