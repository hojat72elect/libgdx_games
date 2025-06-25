

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class MeatPie extends Food {

    {
        image = ItemSpriteSheet.MEAT_PIE;
        energy = Hunger.STARVING * 2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        Buff.affect(hero, WellFed.class).reset();
    }

    @Override
    public int value() {
        return 40 * quantity;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean pasty = false;
            boolean ration = false;
            boolean meat = false;

            for (Item ingredient : ingredients) {
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Pasty || ingredient instanceof PhantomMeat) {
                        pasty = true;
                    } else if (ingredient.getClass() == Food.class) {
                        ration = true;
                    } else if (ingredient instanceof MysteryMeat
                            || ingredient instanceof StewedMeat
                            || ingredient instanceof ChargrilledMeat
                            || ingredient instanceof FrozenCarpaccio) {
                        meat = true;
                    }
                }
            }

            return pasty && ration && meat;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 6;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            for (Item ingredient : ingredients) {
                ingredient.quantity(ingredient.quantity() - 1);
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new MeatPie();
        }
    }
}
