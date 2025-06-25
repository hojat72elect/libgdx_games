

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class Trinket extends Item {

    {
        levelKnown = true;

        unique = true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    protected abstract int upgradeEnergyCost();

    protected static int trinketLevel(Class<? extends Trinket> trinketType) {
        if (Dungeon.hero == null || Dungeon.hero.belongings == null) {
            return -1;
        }

        Trinket trinket = Dungeon.hero.belongings.getItem(trinketType);

        if (trinket != null) {
            return trinket.buffedLvl();
        } else {
            return -1;
        }
    }

    @Override
    public String info() {
        String info = super.info();
        info += "\n\n" + statsDesc();
        return info;
    }

    public abstract String statsDesc();

    public int energyVal() {
        return 5;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        levelKnown = cursedKnown = true; //for pre-2.5 saves
    }

    public static class PlaceHolder extends Trinket {

        {
            image = ItemSpriteSheet.TRINKET_HOLDER;
        }

        @Override
        protected int upgradeEnergyCost() {
            return 0;
        }

        @Override
        public boolean isSimilar(Item item) {
            return item instanceof Trinket;
        }

        @Override
        public String info() {
            return "";
        }

        @Override
        public String statsDesc() {
            return "";
        }
    }

    public static class UpgradeTrinket extends Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0) instanceof Trinket && ingredients.get(0).level() < 3;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return ((Trinket) ingredients.get(0)).upgradeEnergyCost();
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item result = ingredients.get(0).duplicate();
            ingredients.get(0).quantity(0);
            result.upgrade();

            Catalog.countUse(result.getClass());

            return result;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return ingredients.get(0).duplicate().upgrade();
        }
    }
}
