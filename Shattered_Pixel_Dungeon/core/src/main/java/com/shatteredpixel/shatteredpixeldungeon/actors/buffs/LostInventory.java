

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class LostInventory extends Buff {

    {
        type = buffType.NEGATIVE;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            if (target instanceof Hero && ((Hero) target).belongings != null) {
                ((Hero) target).belongings.lostInventory(true);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (target instanceof Hero && ((Hero) target).belongings != null) {
            ((Hero) target).belongings.lostInventory(false);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.NOINV;
    }
}
