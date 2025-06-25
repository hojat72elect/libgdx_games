

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingHammer extends MissileWeapon {

    {
        image = ItemSpriteSheet.THROWING_HAMMER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        baseUses = 15;
        sticky = false;
    }

    @Override
    public int max(int lvl) {
        return 4 * tier +                  //20 base, down from 25
                (tier) * lvl;               //scaling unchanged
    }
}
