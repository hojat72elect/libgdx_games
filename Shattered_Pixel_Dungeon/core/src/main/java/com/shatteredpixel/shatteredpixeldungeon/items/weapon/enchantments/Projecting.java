

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Projecting extends Weapon.Enchantment {

    private static final ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x8844CC);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        //Does nothing as a proc, instead increases weapon range.
        //See weapon.reachFactor, and MissileWeapon.throwPos;
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PURPLE;
    }
}
