

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

//currently only applies to the hero
public class GreaterHaste extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int left;

    @Override
    public boolean act() {

        spendMove();

        spend(TICK);
        return true;
    }

    public void spendMove() {
        left--;
        if (left <= 0) {
            detach();
        }
    }

    public void set(int time) {
        left = time;
    }

    public void extend(float duration) {
        left += duration;
    }

    @Override
    public int icon() {
        return BuffIndicator.HASTE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.3f, 0f);
    }

    @Override
    public float iconFadePercent() {
        //currently tied to the lethal haste talent, as that's the only source
        float duration = 1 + 2 * Dungeon.hero.pointsInTalent(Talent.LETHAL_HASTE);
        return Math.max(0, (duration - left) / duration);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", left);
    }

    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getInt(LEFT);
    }
}
