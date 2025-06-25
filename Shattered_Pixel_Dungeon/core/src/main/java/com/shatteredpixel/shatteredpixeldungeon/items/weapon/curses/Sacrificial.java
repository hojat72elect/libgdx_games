

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Sacrificial extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        float procChance = 1 / 10f * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {
            float missingPercent = attacker.HP / (float) attacker.HT;
            float bleedAmt = (float) (Math.pow(missingPercent, 2) * attacker.HT) / 8f;
            if (Random.Float() < bleedAmt) {
                Buff.affect(attacker, Bleeding.class).set(Math.max(1, bleedAmt), getClass());
            }
        }

        return damage;
    }

    @Override
    public boolean curse() {
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
