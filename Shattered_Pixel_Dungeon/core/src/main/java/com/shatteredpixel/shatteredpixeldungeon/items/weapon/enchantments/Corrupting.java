

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Corrupting extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x440066);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.buffedLvl());

        // lvl 0 - 20%
        // lvl 1 ~ 23%
        // lvl 2 ~ 26%
        float procChance = (level + 5f) / (level + 25f) * procChanceMultiplier(attacker);
        if (damage >= defender.HP
                && Random.Float() < procChance
                && !defender.isImmune(Corruption.class)
                && defender.buff(Corruption.class) == null
                && defender instanceof Mob
                && defender.isAlive()) {

            Mob enemy = (Mob) defender;
            Hero hero = (attacker instanceof Hero) ? (Hero) attacker : Dungeon.hero;

            Corruption.corruptionHeal(enemy);

            AllyBuff.affectAndLoot(enemy, hero, Corruption.class);

            float powerMulti = Math.max(1f, procChance);
            if (powerMulti > 1.1f) {
                //1 turn of adrenaline for each 20% above 100% proc rate
                Buff.affect(enemy, Adrenaline.class, Math.round(5 * (powerMulti - 1f)));
            }

            return 0;
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
