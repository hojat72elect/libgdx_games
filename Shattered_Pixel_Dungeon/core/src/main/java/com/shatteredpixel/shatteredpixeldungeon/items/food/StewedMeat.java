

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class StewedMeat extends Food {

    {
        image = ItemSpriteSheet.STEWED;
        energy = Hunger.HUNGRY / 2f;
    }

    @Override
    public int value() {
        return 8 * quantity;
    }

    public static class oneMeat extends Recipe.SimpleRecipe {
        {
            inputs = new Class[]{MysteryMeat.class};
            inQuantity = new int[]{1};

            cost = 1;

            output = StewedMeat.class;
            outQuantity = 1;
        }
    }

    public static class twoMeat extends Recipe.SimpleRecipe {
        {
            inputs = new Class[]{MysteryMeat.class};
            inQuantity = new int[]{2};

            cost = 2;

            output = StewedMeat.class;
            outQuantity = 2;
        }
    }

    //red meat
    //blue meat

    public static class threeMeat extends Recipe.SimpleRecipe {
        {
            inputs = new Class[]{MysteryMeat.class};
            inQuantity = new int[]{3};

            cost = 2;

            output = StewedMeat.class;
            outQuantity = 3;
        }
    }
}
