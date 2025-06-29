

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class ExoticScroll extends Scroll {


    public static final LinkedHashMap<Class<? extends Scroll>, Class<? extends ExoticScroll>> regToExo = new LinkedHashMap<>();
    public static final LinkedHashMap<Class<? extends ExoticScroll>, Class<? extends Scroll>> exoToReg = new LinkedHashMap<>();

    static {
        regToExo.put(ScrollOfUpgrade.class, ScrollOfEnchantment.class);
        exoToReg.put(ScrollOfEnchantment.class, ScrollOfUpgrade.class);

        regToExo.put(ScrollOfIdentify.class, ScrollOfDivination.class);
        exoToReg.put(ScrollOfDivination.class, ScrollOfIdentify.class);

        regToExo.put(ScrollOfRemoveCurse.class, ScrollOfAntiMagic.class);
        exoToReg.put(ScrollOfAntiMagic.class, ScrollOfRemoveCurse.class);

        regToExo.put(ScrollOfMirrorImage.class, ScrollOfPrismaticImage.class);
        exoToReg.put(ScrollOfPrismaticImage.class, ScrollOfMirrorImage.class);

        regToExo.put(ScrollOfRecharging.class, ScrollOfMysticalEnergy.class);
        exoToReg.put(ScrollOfMysticalEnergy.class, ScrollOfRecharging.class);

        regToExo.put(ScrollOfTeleportation.class, ScrollOfPassage.class);
        exoToReg.put(ScrollOfPassage.class, ScrollOfTeleportation.class);

        regToExo.put(ScrollOfLullaby.class, ScrollOfSirensSong.class);
        exoToReg.put(ScrollOfSirensSong.class, ScrollOfLullaby.class);

        regToExo.put(ScrollOfMagicMapping.class, ScrollOfForesight.class);
        exoToReg.put(ScrollOfForesight.class, ScrollOfMagicMapping.class);

        regToExo.put(ScrollOfRage.class, ScrollOfChallenge.class);
        exoToReg.put(ScrollOfChallenge.class, ScrollOfRage.class);

        regToExo.put(ScrollOfRetribution.class, ScrollOfPsionicBlast.class);
        exoToReg.put(ScrollOfPsionicBlast.class, ScrollOfRetribution.class);

        regToExo.put(ScrollOfTerror.class, ScrollOfDread.class);
        exoToReg.put(ScrollOfDread.class, ScrollOfTerror.class);

        regToExo.put(ScrollOfTransmutation.class, ScrollOfMetamorphosis.class);
        exoToReg.put(ScrollOfMetamorphosis.class, ScrollOfTransmutation.class);
    }

    @Override
    public boolean isKnown() {
        return anonymous || (handler != null && handler.isKnown(exoToReg.get(this.getClass())));
    }

    @Override
    public void setKnown() {
        if (!isKnown()) {
            handler.know(exoToReg.get(this.getClass()));
            updateQuickslot();
        }
    }

    @Override
    public void reset() {
        super.reset();
        if (handler != null && handler.contains(exoToReg.get(this.getClass()))) {
            image = handler.image(exoToReg.get(this.getClass())) + 16;
            rune = handler.label(exoToReg.get(this.getClass()));
        }
    }

    @Override
    //20 gold more than its none-exotic equivalent
    public int value() {
        return (Reflection.newInstance(exoToReg.get(getClass())).value() + 30) * quantity;
    }

    @Override
    //6 more energy than its none-exotic equivalent
    public int energyVal() {
        return (Reflection.newInstance(exoToReg.get(getClass())).energyVal() + 6) * quantity;
    }

    public static class ScrollToExotic extends Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && regToExo.containsKey(ingredients.get(0).getClass());
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 6;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            for (Item i : ingredients) {
                i.quantity(i.quantity() - 1);
            }

            return Reflection.newInstance(regToExo.get(ingredients.get(0).getClass()));
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return Reflection.newInstance(regToExo.get(ingredients.get(0).getClass()));
        }
    }
}
