

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Bolas extends MissileWeapon {

    {
        image = ItemSpriteSheet.BOLAS;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1f;

        tier = 3;
        baseUses = 5;
    }

    @Override
    public int max(int lvl) {
        return 3 * tier +                      //9 base, down from 15
                (tier == 1 ? 2 * lvl : tier * lvl); //scaling unchanged
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.prolong(defender, Cripple.class, Cripple.DURATION);
        return super.proc(attacker, defender, damage);
    }
}
