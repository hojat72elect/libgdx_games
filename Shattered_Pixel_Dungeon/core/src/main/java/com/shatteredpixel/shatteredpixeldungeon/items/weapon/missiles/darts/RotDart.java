

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;


public class RotDart extends TippedDart {

    {
        image = ItemSpriteSheet.ROT_DART;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        //when processing charged shot, only corrode enemies
        if (processingChargedShot && attacker.alignment == defender.alignment) {
            //do nothing
        } else if (defender.properties().contains(Char.Property.BOSS)
                || defender.properties().contains(Char.Property.MINIBOSS)) {
            Buff.affect(defender, Corrosion.class).set(5f, Dungeon.scalingDepth() / 3);
        } else {
            Buff.affect(defender, Corrosion.class).set(10f, Dungeon.scalingDepth());
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public float durabilityPerUse() {
        return MAX_DURABILITY / 5f; //always 5 uses
    }
}
