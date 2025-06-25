

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class AdrenalineSurge extends Buff {

    public static float DURATION = 200f;

    {
        type = buffType.POSITIVE;
    }

    private int boost;
    private float interval;

    public void reset(int boost, float interval) {
        this.boost = boost;
        this.interval = interval;
        spend(interval - cooldown());
    }

    public void delay(float value) {
        spend(value);
    }

    public int boost() {
        return boost;
    }

    @Override
    public boolean act() {
        boost--;
        if (boost > 0) {
            spend(interval);
        } else {
            detach();
        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.5f, 0);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) visualcooldown());
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", boost, dispTurns(visualcooldown()));
    }

    private static final String BOOST = "boost";
    private static final String INTERVAL = "interval";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BOOST, boost);
        bundle.put(INTERVAL, interval);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        boost = bundle.getInt(BOOST);
        interval = bundle.getFloat(INTERVAL);
    }
}
