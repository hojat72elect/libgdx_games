

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SmallRation extends Food {

    {
        image = ItemSpriteSheet.OVERPRICED;
        energy = Hunger.HUNGRY / 2f;
    }

    @Override
    public int value() {
        return 10 * quantity;
    }
}
