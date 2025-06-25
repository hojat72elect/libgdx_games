

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class HealingDart extends TippedDart {

    {
        image = ItemSpriteSheet.HEALING_DART;
        usesTargeting = false; //you never want to throw this at an enemy
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        //do nothing to the hero or enemies when processing charged shot
        if (processingChargedShot && (defender == attacker || attacker.alignment != defender.alignment)) {
            return super.proc(attacker, defender, damage);
        }

        //heals 30 hp at base, scaling with enemy HT
        PotionOfHealing.cure(defender);
        Buff.affect(defender, Healing.class).setHeal((int) (0.5f * defender.HT + 30), 0.25f, 0);

        if (attacker.alignment == defender.alignment) {
            return 0;
        }

        return super.proc(attacker, defender, damage);
    }
}
