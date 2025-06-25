

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Affection extends Glyph {

    private static final ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xFF4488);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.buffedLvl());

        // lvl 0 - 15%
        // lvl 1 ~ 19%
        // lvl 2 ~ 23%
        float procChance = (level + 3f) / (level + 20f) * procChanceMultiplier(defender);
        if (Random.Float() < procChance) {

            float powerMulti = Math.max(1f, procChance);

            Buff.affect(attacker, Charm.class, Math.round(Charm.DURATION * powerMulti)).object = defender.id();
            attacker.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return PINK;
    }
}
