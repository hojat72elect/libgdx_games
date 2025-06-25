

package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CrystalKey extends Key {

    {
        image = ItemSpriteSheet.CRYSTAL_KEY;
    }

    public CrystalKey() {
        this(0);
    }

    public CrystalKey(int depth) {
        super();
        this.depth = depth;
    }
}
