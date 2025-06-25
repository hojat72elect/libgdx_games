

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Terror extends FlavourBuff {

    public int object = 0;

    private static final String OBJECT = "object";

    public static final float DURATION = 20f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(OBJECT, object);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        object = bundle.getInt(OBJECT);
    }

    @Override
    public int icon() {
        return BuffIndicator.TERROR;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    public boolean ignoreNextHit = false;

    public void recover() {
        if (ignoreNextHit) {
            ignoreNextHit = false;
            return;
        }
        spend(-5f);
        if (cooldown() <= 0) {
            detach();
        }
    }
}
