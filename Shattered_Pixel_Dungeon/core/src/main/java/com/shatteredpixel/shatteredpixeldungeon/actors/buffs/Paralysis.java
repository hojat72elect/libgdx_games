

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Paralysis extends FlavourBuff {

    public static final float DURATION = 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            target.paralysed++;
            return true;
        } else {
            return false;
        }
    }

    public void processDamage(int damage) {
        if (target == null) return;
        ParalysisResist resist = target.buff(ParalysisResist.class);
        if (resist == null) {
            resist = Buff.affect(target, ParalysisResist.class);
        }
        resist.damage += damage;
        if (Random.NormalIntRange(0, resist.damage) >= Random.NormalIntRange(0, target.HP)) {
            if (Dungeon.level.heroFOV[target.pos]) {
                target.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "out"));
            }
            detach();
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (target.paralysed > 0)
            target.paralysed--;
    }

    @Override
    public int icon() {
        return BuffIndicator.PARALYSIS;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.PARALYSED);
        else if (target.paralysed <= 1) target.sprite.remove(CharSprite.State.PARALYSED);
    }

    public static class ParalysisResist extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private int damage;

        @Override
        public boolean act() {
            if (target.buff(Paralysis.class) == null) {
                damage -= Math.ceil(damage / 10f);
                if (damage >= 0) detach();
            }
            spend(TICK);
            return true;
        }

        private static final String DAMAGE = "damage";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            damage = bundle.getInt(DAMAGE);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            bundle.put(DAMAGE, damage);
        }
    }
}
