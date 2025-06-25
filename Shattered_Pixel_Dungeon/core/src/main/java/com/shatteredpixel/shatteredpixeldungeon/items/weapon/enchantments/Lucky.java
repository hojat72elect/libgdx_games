

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Visual;
import com.watabou.utils.Random;

public class Lucky extends Weapon.Enchantment {

    private static final ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x00FF00);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.buffedLvl());

        // lvl 0 - 10%
        // lvl 1 ~ 12%
        // lvl 2 ~ 14%
        float procChance = (level + 4f) / (level + 40f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            //default is -5: 80% common, 20% uncommon, 0% rare
            //ring level increases by 1 for each 20% above 100% proc rate
            Buff.affect(defender, LuckProc.class).ringLevel = -10 + Math.round(5 * powerMulti);
        } else {
            //in rare cases where we attack many times at once (e.g. gladiator fury)
            // make sure that failed luck procs override prior succeeded ones
            if (defender.buff(LuckProc.class) != null) {
                defender.buff(LuckProc.class).detach();
            }
        }

        return damage;
    }

    public static Item genLoot() {
        //80% common, 20% uncommon, 0% rare
        return RingOfWealth.genConsumableDrop(-5);
    }

    public static void showFlare(Visual vis) {
        RingOfWealth.showFlareForBonusDrop(vis);
    }

    @Override
    public Glowing glowing() {
        return GREEN;
    }

    //used to keep track of whether a luck proc is incoming. see Mob.die()
    public static class LuckProc extends Buff {

        private int ringLevel = -5;

        @Override
        public boolean act() {
            detach();
            return true;
        }

        public Item genLoot() {
            detach();
            return RingOfWealth.genConsumableDrop(ringLevel);
        }
    }
}
