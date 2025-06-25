

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class ElixirOfMight extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_MIGHT;

        unique = true;

        talentFactor = 2f;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        hero.STR++;
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.STRENGTH);

        Buff.affect(hero, HTBoost.class).reset();
        HTBoost boost = Buff.affect(hero, HTBoost.class);
        boost.reset();

        hero.updateHT(true);
        GLog.p(Messages.get(this, "msg", hero.STR()));

        Badges.validateStrengthAttained();
        Badges.validateDuelistUnlock();
    }

    public String desc() {
        return Messages.get(this, "desc", HTBoost.boost(Dungeon.hero != null ? Dungeon.hero.HT : 20));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{PotionOfStrength.class};
            inQuantity = new int[]{1};

            cost = 16;

            output = ElixirOfMight.class;
            outQuantity = 1;
        }
    }

    public static class HTBoost extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private int left;

        public void reset() {
            left = 5;
        }

        public int boost() {
            return Math.round(left * boost(15 + 5 * ((Hero) target).lvl) / 5f);
        }

        public static int boost(int HT) {
            return Math.round(4 + HT / 20f);
        }

        public void onLevelUp() {
            left--;
            if (left <= 0) {
                detach();
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.HEALING;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0.5f, 0f);
        }

        @Override
        public float iconFadePercent() {
            return (5f - left) / 5f;
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(left);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", boost(), left);
        }

        private static final String LEFT = "left";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LEFT, left);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            left = bundle.getInt(LEFT);
        }
    }
}
