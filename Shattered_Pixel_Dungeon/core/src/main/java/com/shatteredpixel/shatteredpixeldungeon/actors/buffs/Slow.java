

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Slow extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public static final float DURATION = 10f;

    @Override
    public int icon() {
        return BuffIndicator.TIME;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.33f, 0.2f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
