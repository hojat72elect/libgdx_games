

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Shuriken extends MissileWeapon {

    {
        image = ItemSpriteSheet.SHURIKEN;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.2f;

        tier = 2;
        baseUses = 5;
    }

    @Override
    public int max(int lvl) {
        return 4 * tier +                      //8 base, down from 10
                (tier == 1 ? 2 * lvl : tier * lvl); //scaling unchanged
    }

    @Override
    public float delayFactor(Char owner) {
        if (owner instanceof Hero && ((Hero) owner).justMoved) return 0;
        else return super.delayFactor(owner);
    }
}
