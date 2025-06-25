

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Unstable extends Weapon.Enchantment {

    private static final ItemSprite.Glowing GREY = new ItemSprite.Glowing(0x999999);

    private static final Class<? extends Weapon.Enchantment>[] randomEnchants = new Class[]{
            Blazing.class,
            Blocking.class,
            Blooming.class,
            Chilling.class,
            Kinetic.class,
            Corrupting.class,
            Elastic.class,
            Grim.class,
            Lucky.class,
            //projecting not included, no on-hit effect
            Shocking.class,
            Vampiric.class
    };

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        int conservedDamage = 0;
        if (attacker.buff(Kinetic.ConservedDamage.class) != null) {
            conservedDamage = attacker.buff(Kinetic.ConservedDamage.class).damageBonus();
            attacker.buff(Kinetic.ConservedDamage.class).detach();
        }

        damage = Reflection.newInstance(Random.oneOf(randomEnchants)).proc(weapon, attacker, defender, damage);

        return damage + conservedDamage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREY;
    }
}
