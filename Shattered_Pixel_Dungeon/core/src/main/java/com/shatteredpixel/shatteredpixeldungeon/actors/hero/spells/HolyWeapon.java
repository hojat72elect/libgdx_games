

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class HolyWeapon extends ClericSpell {

    public static final HolyWeapon INSTANCE = new HolyWeapon();

    @Override
    public int icon() {
        return HeroIcon.HOLY_WEAPON;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 2;
    }

    @Override
    public void onCast(HolyTome tome, Hero hero) {

        Buff.affect(hero, HolyWepBuff.class, 50f);
        Item.updateQuickslot();

        Sample.INSTANCE.play(Assets.Sounds.READ);

        hero.sprite.operate(hero.pos);
        if (hero.belongings.weapon() != null) Enchanting.show(hero, hero.belongings.weapon());

        onSpellCast(tome, hero);
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        if (Dungeon.hero.subClass == HeroSubClass.PALADIN) {
            desc += "\n\n" + Messages.get(this, "desc_paladin");
        }
        return desc + "\n\n" + Messages.get(this, "charge_cost", (int) chargeUse(Dungeon.hero));
    }

    public static class HolyWepBuff extends FlavourBuff {

        public static final float DURATION = 50f;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.HOLY_WEAPON;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String desc() {
            if (Dungeon.hero.subClass == HeroSubClass.PALADIN) {
                return Messages.get(this, "desc_paladin", dispTurns());
            } else {
                return Messages.get(this, "desc", dispTurns());
            }
        }

        @Override
        public void detach() {
            super.detach();
            Item.updateQuickslot();
        }

        public void extend(float extension) {
            if (cooldown() + extension <= 2 * DURATION) {
                spend(extension);
            } else {
                postpone(2 * DURATION);
            }
        }
    }
}
