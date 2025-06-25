

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.watabou.utils.Bundle;

//A buff whose only purposes is to keep track of a count of some form
public class CounterBuff extends Buff {

    private float count = 0;

    public void countUp(float inc) {
        count += inc;
    }

    public void countDown(float inc) {
        count -= inc;
    }

    public float count() {
        return count;
    }

    private static final String COUNT = "count";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT, count);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getFloat(COUNT);
    }
}
