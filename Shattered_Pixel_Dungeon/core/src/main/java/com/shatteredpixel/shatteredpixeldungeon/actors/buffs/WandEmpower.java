

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class WandEmpower extends Buff {

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.WAND;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 1, 0);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (3 - left) / 3f);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dmgBoost, left);
    }

    public int dmgBoost;
    public int left;

    public void set(int dmg, int shots) {
        dmgBoost = dmg;
        left = Math.max(left, shots);
    }

    private static final String BOOST = "boost";
    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BOOST, dmgBoost);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        dmgBoost = bundle.getInt(BOOST);
        left = bundle.getInt(LEFT);
    }
}
