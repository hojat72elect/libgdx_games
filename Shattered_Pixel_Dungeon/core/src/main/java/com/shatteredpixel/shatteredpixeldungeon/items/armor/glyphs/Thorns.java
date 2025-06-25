

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Thorns extends Armor.Glyph {

    private static final ItemSprite.Glowing RED = new ItemSprite.Glowing(0x660022);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.buffedLvl());

        // lvl 0 - 16.7%
        // lvl 1 - 23.1%
        // lvl 2 - 28.5%
        float procChance = (level + 2f) / (level + 12f) * procChanceMultiplier(defender);
        if (attacker.alignment != defender.alignment && Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            Buff.affect(attacker, Bleeding.class).set(Math.round((4 + level) * powerMulti));
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}
