

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Potential extends Glyph {

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF, 0.6f);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.buffedLvl());

        // lvl 0 - 16.7%
        // lvl 1 - 28.6%
        // lvl 2 - 37.5%
        float procChance = (level + 1f) / (level + 6f) * procChanceMultiplier(defender);
        if (Random.Float() < procChance && defender instanceof Hero) {

            float powerMulti = Math.max(1f, procChance);

            int wands = ((Hero) defender).belongings.charge(powerMulti);
            if (wands > 0) {
                defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
            }
        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return WHITE;
    }
}
