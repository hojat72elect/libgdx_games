

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Obfuscation extends Armor.Glyph {

    private static final ItemSprite.Glowing GREY = new ItemSprite.Glowing(0x888888);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, triggered in Char.stealth()
        return damage;
    }

    public static float stealthBoost(Char owner, int level) {
        if (level == -1) {
            return 0;
        } else {
            return (1 + level / 3f) * genericProcChanceMultiplier(owner);
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREY;
    }
}
