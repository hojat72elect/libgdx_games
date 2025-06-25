

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Friendly extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        float procChance = 1 / 10f * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            Buff.affect(attacker, Charm.class, Charm.DURATION).object = defender.id();
            attacker.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);

            Charm c = Buff.affect(defender, Charm.class, Charm.DURATION / 2);
            c.ignoreNextHit = true;
            c.object = attacker.id();
            defender.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
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
