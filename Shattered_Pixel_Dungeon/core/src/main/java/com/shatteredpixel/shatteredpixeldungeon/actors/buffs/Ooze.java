

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ooze extends Buff {

    public static final float DURATION = 20f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    private float left;
    private boolean acted = false; //whether the debuff has done any damage at all yet

    private static final String LEFT = "left";
    private static final String ACTED = "acted";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
        bundle.put(ACTED, acted);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
        acted = bundle.getBoolean(ACTED);
    }

    @Override
    public int icon() {
        return BuffIndicator.OOZE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    public void set(float left) {
        this.left = left;
        acted = false;
    }

    public void extend(float duration) {
        left += duration;
    }

    @Override
    public boolean act() {
        //washing away happens before debuff effects if debuff has gotten to act
        if (acted && Dungeon.level.water[target.pos] && !target.flying) {
            detach();
        } else if (target.isAlive()) {

            acted = true;
            if (Dungeon.scalingDepth() > 5) {
                target.damage(1 + Dungeon.scalingDepth() / 5, this);
            } else if (Dungeon.scalingDepth() == 5) {
                target.damage(1, this); //1 dmg per turn vs Goo
            } else if (Random.Int(2) == 0) {
                target.damage(1, this); //0.5 dmg per turn in sewers
            }

            if (!target.isAlive() && target == Dungeon.hero) {
                Dungeon.fail(this);
                GLog.n(Messages.get(this, "ondeath"));
            }
            spend(TICK);
            left -= TICK;
            if (left <= 0) {
                detach();
            }
        } else {
            detach();
        }
        if (Dungeon.level.water[target.pos] && !target.flying) {
            detach();
        }
        return true;
    }
}
