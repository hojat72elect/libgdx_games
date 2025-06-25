

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionBandolier extends Bag {

    {
        image = ItemSpriteSheet.BANDOLIER;
    }

    @Override
    public boolean canHold(Item item) {
        if (item instanceof Potion || item instanceof LiquidMetal || item instanceof Waterskin) {
            return super.canHold(item);
        } else {
            return false;
        }
    }

    public int capacity() {
        return 19;
    }

    @Override
    public int value() {
        return 40;
    }
}
