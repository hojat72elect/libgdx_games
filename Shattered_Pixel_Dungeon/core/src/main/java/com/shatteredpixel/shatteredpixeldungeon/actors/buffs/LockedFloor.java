

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class LockedFloor extends Buff {

    //the amount of turns remaining before beneficial passive effects turn off
    //starts at 50 turns normally, 20 with badder bosses
    private float left = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 20 : 50;

    @Override
    public boolean act() {
        spend(TICK);

        if (!Dungeon.level.locked)
            detach();

        if (left >= 1)
            left--;

        return true;
    }

    public void addTime(float time) {
        left += time;
        left = Math.min(left, 50); //cannot build to more than 50
    }

    public void removeTime(float time) {
        left -= time; //can go negative!
    }

    public boolean regenOn() {
        return left >= 1;
    }

    private final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
    }

    @Override
    public int icon() {
        return BuffIndicator.LOCKED_FLOOR;
    }
}
