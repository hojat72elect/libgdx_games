

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class ElixirOfArcaneArmor extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_ARCANE;
    }

    @Override
    public void apply(Hero hero) {
        Buff.affect(hero, ArcaneArmor.class).set(5 + hero.lvl / 2, 80);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{PotionOfEarthenArmor.class, GooBlob.class};
            inQuantity = new int[]{1, 1};

            cost = 8;

            output = ElixirOfArcaneArmor.class;
            outQuantity = 1;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Catalog.countUse(GooBlob.class);
            return super.brew(ingredients);
        }
    }
}
