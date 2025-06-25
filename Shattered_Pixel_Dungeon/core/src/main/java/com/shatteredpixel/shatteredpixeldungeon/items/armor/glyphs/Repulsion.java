

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Repulsion extends Armor.Glyph {

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.buffedLvl());

        // lvl 0 - 20%
        // lvl 1 - 33%
        // lvl 2 - 43%
        float procChance = (level + 1f) / (level + 5f) * procChanceMultiplier(defender);
        if (Dungeon.level.adjacent(attacker.pos, defender.pos) && Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            int oppositeHero = attacker.pos + (attacker.pos - defender.pos);
            Ballistica trajectory = new Ballistica(attacker.pos, oppositeHero, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(attacker,
                    trajectory,
                    Math.round(2 * powerMulti),
                    true,
                    true,
                    this);
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }
}
