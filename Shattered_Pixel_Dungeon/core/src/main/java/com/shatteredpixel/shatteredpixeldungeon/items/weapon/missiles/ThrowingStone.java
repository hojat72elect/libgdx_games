

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingStone extends MissileWeapon {

    {
        image = ItemSpriteSheet.THROWING_STONE;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.1f;

        bones = false;

        tier = 1;
        baseUses = 5;
        sticky = false;
    }

    @Override
    public int value() {
        return super.value() / 2; //half normal value
    }
}
