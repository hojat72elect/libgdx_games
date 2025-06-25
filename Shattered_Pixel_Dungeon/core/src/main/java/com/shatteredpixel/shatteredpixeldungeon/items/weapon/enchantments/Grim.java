

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;

public class Grim extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

        if (defender.isImmune(Grim.class)) {
            return damage;
        }

        int level = Math.max(0, weapon.buffedLvl());

        //scales from 0 - 50% based on how low hp the enemy is, plus 0-5% per level
        float maxChance = 0.5f + .05f * level;
        maxChance *= procChanceMultiplier(attacker);

        //we defer logic using an actor here so we can know the true final damage
        //see Char.damage
        Buff.affect(defender, GrimTracker.class).maxChance = maxChance;

        if (defender.buff(GrimTracker.class) != null
                && attacker instanceof Hero
                && weapon.hasEnchant(Grim.class, attacker)) {
            defender.buff(GrimTracker.class).qualifiesForBadge = true;
        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return BLACK;
    }

    public static class GrimTracker extends Buff {

        {
            actPriority = Actor.VFX_PRIO;
        }

        public float maxChance;
        public boolean qualifiesForBadge;

        @Override
        public boolean act() {
            detach();
            return true;
        }
    }
}
