

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class Wayward extends Weapon.Enchantment {

    private static final ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        float procChance = 1 / 4f * procChanceMultiplier(attacker);

        if (attacker.buff(WaywardBuff.class) != null) {
            Buff.detach(attacker, WaywardBuff.class);
        } else if (Random.Float() < procChance) {
            Buff.prolong(attacker, WaywardBuff.class, WaywardBuff.DURATION);
        }

        return damage;
    }

    @Override
    public boolean curse() {
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    //see weapon.accuracyFactor for effect
    public static class WaywardBuff extends FlavourBuff {

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        public static final float DURATION = 10f;

        @Override
        public int icon() {
            return BuffIndicator.WEAKNESS;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1, 1, 0);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
