

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ElixirOfDragonsBlood extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_DRAGON;
    }

    @Override
    public void apply(Hero hero) {
        Buff.affect(hero, FireImbue.class).set(FireImbue.DURATION);
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
        hero.sprite.emitter().burst(FlameParticle.FACTORY, 10);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{PotionOfDragonsBreath.class};
            inQuantity = new int[]{1};

            cost = 10;

            output = ElixirOfDragonsBlood.class;
            outQuantity = 1;
        }
    }
}
