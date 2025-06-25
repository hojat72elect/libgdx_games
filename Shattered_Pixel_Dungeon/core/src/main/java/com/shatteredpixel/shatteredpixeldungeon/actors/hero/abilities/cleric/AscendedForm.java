

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.DivineIntervention;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class AscendedForm extends ArmorAbility {

    {
        baseChargeUse = 50;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {

        Buff.affect(hero, AscendBuff.class).reset();
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        new Flare(6, 48).color(0xFFFF00, true).show(hero.sprite, 2f);

        armor.charge -= chargeUse(hero);
        Item.updateQuickslot();
        Invisibility.dispel();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.ASCENDED_FORM;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.DIVINE_INTERVENTION, Talent.JUDGEMENT, Talent.FLASH, Talent.HEROIC_ENERGY};
    }

    public static class AscendBuff extends ShieldBuff {

        {
            type = buffType.POSITIVE;

            detachesAtZero = false;
            shieldUsePriority = 1;
        }

        public static float DURATION = 10f;

        @Override
        public int icon() {
            return BuffIndicator.ASCEND;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - left) / DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(left);
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.GLOWING);
            else target.sprite.remove(CharSprite.State.GLOWING);
        }

        public int left = 10;
        public int spellCasts = 0;
        public int flashCasts = 0;
        public boolean divineInverventionCast = false;

        public void reset() {
            setShield(30);
            left = (int) DURATION;
        }

        public void extend(int amt) {
            left += amt;
        }

        @Override
        public boolean act() {
            left--;
            if (left <= 0) {
                detach();
                for (Char ch : Actor.chars()) {
                    if (ch.buff(DivineIntervention.DivineShield.class) != null) {
                        ch.buff(DivineIntervention.DivineShield.class).detach();
                    }
                }
                return true;
            }

            spend(TICK);
            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", shielding(), left);
        }

        public static final String LEFT = "left";
        public static final String SPELL_CASTS = "spell_casts";
        public static final String FLASH_CASTS = "flash_casts";
        public static final String DIVINE_INTERVENTION_CAST = "divine_intervention_cast";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LEFT, left);
            bundle.put(SPELL_CASTS, spellCasts);
            bundle.put(FLASH_CASTS, flashCasts);
            bundle.put(DIVINE_INTERVENTION_CAST, divineInverventionCast);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            left = bundle.getInt(LEFT);
            spellCasts = bundle.getInt(SPELL_CASTS);
            flashCasts = bundle.getInt(FLASH_CASTS);
            divineInverventionCast = bundle.getBoolean(DIVINE_INTERVENTION_CAST);
        }
    }
}
