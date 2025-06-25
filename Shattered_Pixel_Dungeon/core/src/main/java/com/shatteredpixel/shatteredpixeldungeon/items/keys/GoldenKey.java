

package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class GoldenKey extends Key {

    {
        image = ItemSpriteSheet.GOLDEN_KEY;
    }

    public GoldenKey() {
        this(0);
    }

    public GoldenKey(int depth) {
        super();
        this.depth = depth;
    }
}
