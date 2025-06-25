

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

public class ElixirOfFeatherFall extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_FEATHER;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    @Override
    public void apply(Hero hero) {
        Buff.append(hero, FeatherBuff.class, FeatherBuff.DURATION);

        hero.sprite.emitter().burst(Speck.factory(Speck.JET), 20);
        GLog.p(Messages.get(this, "light"));
    }

    public static class FeatherBuff extends FlavourBuff {
        //does nothing, just waits to be triggered by chasm falling
        {
            type = buffType.POSITIVE;
        }

        public void processFall() {
            spend(-10f);
            if (cooldown() <= 0) {
                detach();
            }
        }

        public static final float DURATION = 50f;

        @Override
        public int icon() {
            return BuffIndicator.LEVITATION;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 2f, 1.25f);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    @Override
    public int value() {
        return (int) (60 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int) (12 * (quantity / (float) Recipe.OUT_QUANTITY));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 1;

        {
            inputs = new Class[]{PotionOfLevitation.class};
            inQuantity = new int[]{1};

            cost = 10;

            output = ElixirOfFeatherFall.class;
            outQuantity = OUT_QUANTITY;
        }
    }
}
