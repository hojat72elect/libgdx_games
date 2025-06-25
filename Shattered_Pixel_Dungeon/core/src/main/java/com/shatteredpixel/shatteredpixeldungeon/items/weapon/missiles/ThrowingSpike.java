

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingSpike extends MissileWeapon {

    {
        image = ItemSpriteSheet.THROWING_SPIKE;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.2f;

        bones = false;

        tier = 1;
    }
}
