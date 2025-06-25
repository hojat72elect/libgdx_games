

package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class IronKey extends Key {

    {
        image = ItemSpriteSheet.IRON_KEY;
    }

    public IronKey() {
        this(0);
    }

    public IronKey(int depth) {
        super();
        this.depth = depth;
    }
}
