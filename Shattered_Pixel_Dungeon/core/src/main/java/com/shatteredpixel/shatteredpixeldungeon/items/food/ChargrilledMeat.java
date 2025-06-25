

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ChargrilledMeat extends Food {

    {
        image = ItemSpriteSheet.STEAK;
        energy = Hunger.HUNGRY / 2f;
    }

    @Override
    public int value() {
        return 8 * quantity;
    }

    public static Food cook(int quantity) {
        ChargrilledMeat result = new ChargrilledMeat();
        result.quantity = quantity;
        return result;
    }
}
