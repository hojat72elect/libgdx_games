

package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Bulk extends Armor.Glyph {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, triggers in Char.speed()
        return damage;
    }

    //more of a reduction really
    public static float speedBoost(Char owner, int level) {
        if (level == -1 ||
                (Dungeon.level.map[owner.pos] != Terrain.DOOR && Dungeon.level.map[owner.pos] != Terrain.OPEN_DOOR)) {
            return 1;
        } else {
            return 1 / 3f * genericProcChanceMultiplier(owner);
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public boolean curse() {
        return true;
    }
}
