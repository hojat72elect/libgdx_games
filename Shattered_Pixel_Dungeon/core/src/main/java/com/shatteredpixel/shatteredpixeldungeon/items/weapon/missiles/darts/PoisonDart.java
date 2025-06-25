

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PoisonDart extends TippedDart {

    {
        image = ItemSpriteSheet.POISON_DART;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        //when processing charged shot, only poison enemies
        if (!processingChargedShot || attacker.alignment != defender.alignment) {
            Buff.affect(defender, Poison.class).set(3 + Dungeon.scalingDepth() / 2);
        }

        return super.proc(attacker, defender, damage);
    }
}
