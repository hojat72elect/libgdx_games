

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;


public class BlindingDart extends TippedDart {

    {
        image = ItemSpriteSheet.BLINDING_DART;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        //when processing charged shot, only blind enemies
        if (!processingChargedShot || attacker.alignment != defender.alignment) {
            Buff.affect(defender, Blindness.class, Blindness.DURATION);
        }

        return super.proc(attacker, defender, damage);
    }
}
