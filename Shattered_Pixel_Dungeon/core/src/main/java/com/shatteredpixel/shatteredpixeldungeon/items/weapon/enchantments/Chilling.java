

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Chilling extends Weapon.Enchantment {

    private static final ItemSprite.Glowing TEAL = new ItemSprite.Glowing(0x00FFFF);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.buffedLvl());

        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        float procChance = (level + 1f) / (level + 4f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            //adds 3 turns of chill per proc, with a cap of 6 turns
            float durationToAdd = 3f * powerMulti;
            Chill existing = defender.buff(Chill.class);
            if (existing != null) {
                durationToAdd = Math.min(durationToAdd, (6f * powerMulti) - existing.cooldown());
            }

            if (durationToAdd > 0) {
                Buff.affect(defender, Chill.class, durationToAdd);
            }
            Splash.at(defender.sprite.center(), 0xFFB2D6FF, 5);
        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return TEAL;
    }
}
