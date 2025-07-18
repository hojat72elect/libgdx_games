

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BruteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Brute extends Mob {

    {
        spriteClass = BruteSprite.class;

        HP = HT = 40;
        defenseSkill = 15;

        EXP = 8;
        maxLvl = 16;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    protected boolean hasRaged = false;

    @Override
    public int damageRoll() {
        return buff(BruteRage.class) != null ?
                Random.NormalIntRange(15, 40) :
                Random.NormalIntRange(5, 25);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 8);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        if (cause == Chasm.class) {
            hasRaged = true; //don't let enrage trigger for chasm deaths
        }
    }

    @Override
    public synchronized boolean isAlive() {
        if (super.isAlive()) {
            return true;
        } else {
            if (!hasRaged) {
                triggerEnrage();
            }
            return !buffs(BruteRage.class).isEmpty();
        }
    }

    protected void triggerEnrage() {
        Buff.affect(this, BruteRage.class).setShield(HT / 2 + 4);
        sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(HT / 2 + 4), FloatingText.SHIELDING);
        if (Dungeon.level.heroFOV[pos]) {
            SpellSprite.show(this, SpellSprite.BERSERK);
        }
        spend(TICK);
        hasRaged = true;
    }

    private static final String HAS_RAGED = "has_raged";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HAS_RAGED, hasRaged);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hasRaged = bundle.getBoolean(HAS_RAGED);
    }

    public static class BruteRage extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {

            if (target.HP > 0) {
                detach();
                return true;
            }

            absorbDamage(Math.round(4 * AscensionChallenge.statModifier(target)));

            if (shielding() <= 0) {
                target.die(null);
            }

            spend(TICK);

            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.FURY;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", shielding());
        }
    }
}
