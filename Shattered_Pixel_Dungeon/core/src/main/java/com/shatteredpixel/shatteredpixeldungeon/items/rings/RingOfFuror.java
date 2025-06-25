

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfFuror extends Ring {

    {
        icon = ItemSpriteSheet.Icons.RING_FUROR;
        buffClass = Furor.class;
    }

    public String statsInfo() {
        if (isIdentified()) {
            String info = Messages.get(this, "stats",
                    Messages.decimalFormat("#.##", 100f * (Math.pow(1.09051f, soloBuffedBonus()) - 1f)));
            if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)) {
                info += "\n\n" + Messages.get(this, "combined_stats",
                        Messages.decimalFormat("#.##", 100f * (Math.pow(1.09051f, combinedBuffedBonus(Dungeon.hero)) - 1f)));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 9.051f));
        }
    }

    public String upgradeStat1(int level) {
        if (cursed && cursedKnown) level = Math.min(-1, level - 3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.09051f, level + 1) - 1f)) + "%";
    }

    @Override
    protected RingBuff buff() {
        return new Furor();
    }

    public static float attackSpeedMultiplier(Char target) {
        return (float) Math.pow(1.09051, getBuffedBonus(target, Furor.class));
    }

    public class Furor extends RingBuff {
    }
}
