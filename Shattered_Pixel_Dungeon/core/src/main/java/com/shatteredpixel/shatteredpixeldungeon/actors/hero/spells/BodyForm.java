

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class BodyForm extends ClericSpell {

    public static BodyForm INSTANCE = new BodyForm();

    @Override
    public int icon() {
        return HeroIcon.BODY_FORM;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", duration()) + "\n\n" + Messages.get(this, "charge_cost", (int) chargeUse(Dungeon.hero));
    }

    @Override
    public float chargeUse(Hero hero) {
        return 2;
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.BODY_FORM);
    }

    @Override
    public void onCast(HolyTome tome, Hero hero) {

        GameScene.show(new Trinity.WndItemtypeSelect(tome, this));
    }

    public static int duration() {
        return Math.round(13.33f + 6.67f * Dungeon.hero.pointsInTalent(Talent.BODY_FORM));
    }

    public static class BodyFormBuff extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        private Bundlable effect;

        @Override
        public int icon() {
            return BuffIndicator.TRINITY_FORM;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1, 0, 0);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (duration() - visualcooldown()) / duration());
        }

        public void setEffect(Bundlable effect) {
            this.effect = effect;
        }

        public Weapon.Enchantment enchant() {
            if (effect instanceof Weapon.Enchantment) {
                return (Weapon.Enchantment) effect;
            }
            return null;
        }

        public Armor.Glyph glyph() {
            if (effect instanceof Armor.Glyph) {
                return (Armor.Glyph) effect;
            }
            return null;
        }

        @Override
        public String desc() {
            if (enchant() != null) {
                return Messages.get(this, "desc", Messages.titleCase(enchant().name()), dispTurns());
            } else if (glyph() != null) {
                return Messages.get(this, "desc", Messages.titleCase(glyph().name()), dispTurns());
            }
            return super.desc();
        }

        private static final String EFFECT = "effect";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(EFFECT, effect);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            effect = bundle.get(EFFECT);
        }
    }
}
