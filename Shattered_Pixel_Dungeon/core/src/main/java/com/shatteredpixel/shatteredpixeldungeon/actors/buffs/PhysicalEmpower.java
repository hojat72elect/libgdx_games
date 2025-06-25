

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class PhysicalEmpower extends Buff {

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 0.5f, 0);
    }

    @Override
    public float iconFadePercent() {
        float max = 1 + Dungeon.hero.pointsInTalent(Talent.STRENGTHENING_MEAL);
        return Math.max(0, (max - left) / max);
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

    public void set(int dmg, int hits) {
        if (dmg * hits > dmgBoost * left) {
            dmgBoost = dmg;
            left = hits;
        }
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
