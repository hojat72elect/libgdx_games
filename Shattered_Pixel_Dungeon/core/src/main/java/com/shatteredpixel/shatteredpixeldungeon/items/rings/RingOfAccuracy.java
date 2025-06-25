

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfAccuracy extends Ring {

    {
        icon = ItemSpriteSheet.Icons.RING_ACCURACY;
        buffClass = Accuracy.class;
    }

    public String statsInfo() {
        if (isIdentified()) {
            String info = Messages.get(this, "stats",
                    Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, soloBuffedBonus()) - 1f)));
            if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)) {
                info += "\n\n" + Messages.get(this, "combined_stats",
                        Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, combinedBuffedBonus(Dungeon.hero)) - 1f)));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 30f));
        }
    }

    public String upgradeStat1(int level) {
        if (cursed && cursedKnown) level = Math.min(-1, level - 3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, level + 1) - 1f)) + "%";
    }

    @Override
    protected RingBuff buff() {
        return new Accuracy();
    }

    public static float accuracyMultiplier(Char target) {
        return (float) Math.pow(1.3f, getBuffedBonus(target, Accuracy.class));
    }

    public class Accuracy extends RingBuff {
    }
}
