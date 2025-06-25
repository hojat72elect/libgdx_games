

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class VelvetPouch extends Bag {

    {
        image = ItemSpriteSheet.POUCH;
    }

    @Override
    public boolean canHold(Item item) {
        if (item instanceof Plant.Seed || item instanceof Runestone
                || item instanceof GooBlob || item instanceof MetalShard) {
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
        return 30;
    }
}
