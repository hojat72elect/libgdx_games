

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Degrade extends FlavourBuff {

    public static final float DURATION = 30f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            Item.updateQuickslot();
            if (target == Dungeon.hero) ((Hero) target).updateHT(false);
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        if (target == Dungeon.hero) ((Hero) target).updateHT(false);
        Item.updateQuickslot();
    }

    //called in Item.buffedLevel()
    public static int reduceLevel(int level) {
        if (level <= 0) {
            //zero or negative levels are unaffected
            return level;
        } else {
            //Otherwise returns the rounded result of sqrt(2*(lvl-1)) + 1
            // This means that levels 1/2/3/4/5/6/7/8/9/10/11/12/...
            // Are now instead:       1/2/3/3/4/4/4/5/5/ 5/ 5/ 6/...
            // Basically every level starting with 3 sticks around for 1 level longer than the last
            return (int) Math.round(Math.sqrt(2 * (level - 1)) + 1);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.DEGRADE;
    }

    @Override
    public float iconFadePercent() {
        return (DURATION - visualcooldown()) / DURATION;
    }
}
