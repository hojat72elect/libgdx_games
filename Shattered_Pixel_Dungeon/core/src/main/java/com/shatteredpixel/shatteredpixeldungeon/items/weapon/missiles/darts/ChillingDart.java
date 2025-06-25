

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ChillingDart extends TippedDart {

    {
        image = ItemSpriteSheet.CHILLING_DART;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        //when processing charged shot, only chill enemies
        if (!processingChargedShot || attacker.alignment != defender.alignment) {
            if (Dungeon.level.water[defender.pos]) {
                Buff.prolong(defender, Chill.class, Chill.DURATION);
            } else {
                Buff.prolong(defender, Chill.class, 6f);
            }
        }

        return super.proc(attacker, defender, damage);
    }
}
