

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ClothArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_CLOTH;

        bones = false; //Finding them in bones would be semi-frequent and disappointing.
    }

    public ClothArmor() {
        super(1);
    }
}
