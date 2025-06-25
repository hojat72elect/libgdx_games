

package com.shatteredpixel.shatteredpixeldungeon.items.potions.brews;

import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AquaBrew extends Brew {

    {
        image = ItemSpriteSheet.BREW_AQUA;

        talentChance = 1 / (float) Recipe.OUT_QUANTITY;
    }

    @Override
    public void shatter(int cell) {
        GeyserTrap geyser = new GeyserTrap();
        geyser.pos = cell;
        geyser.source = this;

        int userPos = curUser == null ? cell : curUser.pos;
        if (userPos != cell) {
            Ballistica aim = new Ballistica(userPos, cell, Ballistica.STOP_TARGET);
            if (aim.path.size() > aim.dist + 1) {
                geyser.centerKnockBackDirection = aim.path.get(aim.dist + 1);
            }
        }
        geyser.activate();
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

        private static final int OUT_QUANTITY = 8;

        {
            inputs = new Class[]{PotionOfStormClouds.class};
            inQuantity = new int[]{1};

            cost = 8;

            output = AquaBrew.class;
            outQuantity = OUT_QUANTITY;
        }
    }
}
