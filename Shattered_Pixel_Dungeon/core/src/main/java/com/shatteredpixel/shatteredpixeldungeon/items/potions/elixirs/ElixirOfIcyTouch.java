

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ElixirOfIcyTouch extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_ICY;
    }

    @Override
    public void apply(Hero hero) {
        Buff.prolong(hero, FrostImbue.class, FrostImbue.DURATION);
        hero.sprite.emitter().burst(SnowParticle.FACTORY, 5);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{PotionOfSnapFreeze.class};
            inQuantity = new int[]{1};

            cost = 6;

            output = ElixirOfIcyTouch.class;
            outQuantity = 1;
        }
    }
}
