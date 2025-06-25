

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Tomahawk extends MissileWeapon {

    {
        image = ItemSpriteSheet.TOMAHAWK;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.9f;

        tier = 4;
        baseUses = 5;
    }

    @Override
    public int min(int lvl) {
        return Math.round(1.5f * tier) +   //6 base, down from 8
                2 * lvl;                    //scaling unchanged
    }

    @Override
    public int max(int lvl) {
        return Math.round(3.75f * tier) +  //15 base, down from 20
                (tier) * lvl;                 //scaling unchanged
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Bleeding.class).set(Math.round(damage * 0.6f));
        return super.proc(attacker, defender, damage);
    }
}
