

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class NaturesPower extends ArmorAbility {

    {
        baseChargeUse = 35f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {

        Buff.prolong(hero, naturesPowerTracker.class, naturesPowerTracker.DURATION);
        hero.buff(naturesPowerTracker.class).extensionsLeft = 2;
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        hero.sprite.emitter().burst(LeafParticle.GENERAL, 10);

        armor.charge -= chargeUse(hero);
        Item.updateQuickslot();
        Invisibility.dispel();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.NATURES_POWER;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.GROWING_POWER, Talent.NATURES_WRATH, Talent.WILD_MOMENTUM, Talent.HEROIC_ENERGY};
    }

    public static class naturesPowerTracker extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 8f;

        public int extensionsLeft = 2;

        public void extend(int turns) {
            if (extensionsLeft > 0 && turns > 0) {
                spend(turns);
                extensionsLeft--;
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.NATURE_POWER;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
