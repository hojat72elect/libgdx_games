

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ElixirOfHoneyedHealing extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_HONEY;
    }

    @Override
    public void apply(Hero hero) {
        PotionOfHealing.cure(hero);
        PotionOfHealing.heal(hero);
        Buff.affect(hero, Hunger.class).satisfy(Hunger.HUNGRY / 2f);
        Talent.onFoodEaten(hero, Hunger.HUNGRY / 2f, this);
    }

    @Override
    public void shatter(int cell) {
        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        Char ch = Actor.findChar(cell);
        if (ch != null) {
            PotionOfHealing.cure(ch);
            PotionOfHealing.heal(ch);
            if (ch instanceof Bee && ch.alignment != curUser.alignment) {
                ch.alignment = Char.Alignment.ALLY;
                ((Bee) ch).setPotInfo(-1, null);
            }
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{PotionOfHealing.class, Honeypot.ShatteredPot.class};
            inQuantity = new int[]{1, 1};

            cost = 4;

            output = ElixirOfHoneyedHealing.class;
            outQuantity = 1;
        }
    }
}
