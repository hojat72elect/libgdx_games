

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Recharging extends FlavourBuff {

    public static final float DURATION = 30f;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.RECHARGING;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 1, 0);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    //want to process partial turns for this buff, and not count it when it's expiring.
    //firstly, if this buff has half a turn left, should give out half the benefit.
    //secondly, recall that buffs execute in random order, so this can cause a problem where we can't simply check
    //if this buff is still attached, must instead directly check its remaining time, and act accordingly.
    //otherwise this causes inconsistent behaviour where this may detach before, or after, a wand charger acts.
    public float remainder() {
        return Math.min(1f, this.cooldown());
    }
}
