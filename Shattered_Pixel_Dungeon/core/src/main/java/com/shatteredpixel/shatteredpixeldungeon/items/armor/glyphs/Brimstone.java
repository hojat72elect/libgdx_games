

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Brimstone extends Armor.Glyph {

    private static final ItemSprite.Glowing ORANGE = new ItemSprite.Glowing(0xFF4400);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, triggers in Char.isImmune
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }
}
