

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingClub extends MissileWeapon {

    {
        image = ItemSpriteSheet.THROWING_CLUB;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.1f;

        tier = 2;
        baseUses = 15;
        sticky = false;
    }

    @Override
    public int max(int lvl) {
        return 4 * tier +                  //8 base, down from 10
                (tier) * lvl;               //scaling unchanged
    }
}
