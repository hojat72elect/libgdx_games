

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class HoldFast extends Buff {

    {
        type = buffType.POSITIVE;
    }

    public int pos = -1;

    @Override
    public boolean act() {
        if (pos != target.pos) {
            detach();
        } else {
            spend(TICK);
        }
        return true;
    }

    public int armorBonus() {
        if (pos == target.pos && target instanceof Hero) {
            return Random.NormalIntRange(((Hero) target).pointsInTalent(Talent.HOLD_FAST), 2 * ((Hero) target).pointsInTalent(Talent.HOLD_FAST));
        } else {
            detach();
            return 0;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.ARMOR;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1.9f, 2.4f, 3.25f);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", Dungeon.hero.pointsInTalent(Talent.HOLD_FAST), 2 * Dungeon.hero.pointsInTalent(Talent.HOLD_FAST));
    }

    private static final String POS = "pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POS, pos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        pos = bundle.getInt(POS);
    }
}
