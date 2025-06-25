

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Blazing extends Weapon.Enchantment {

    private static final ItemSprite.Glowing ORANGE = new ItemSprite.Glowing(0xFF4400);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.buffedLvl());

        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        float procChance = (level + 1f) / (level + 3f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            if (defender.buff(Burning.class) == null) {
                Buff.affect(defender, Burning.class).reignite(defender, 8f);
                powerMulti -= 1;
            }

            if (powerMulti > 0) {
                int burnDamage = Random.NormalIntRange(1, 3 + Dungeon.scalingDepth() / 4);
                burnDamage = Math.round(burnDamage * 0.67f * powerMulti);
                if (burnDamage > 0) {
                    defender.damage(burnDamage, this);
                }
            }

            defender.sprite.emitter().burst(FlameParticle.FACTORY, level + 1);
        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return ORANGE;
    }
}
