

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class EyeOfNewt extends Trinket {

    {
        image = ItemSpriteSheet.EYE_OF_NEWT;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 8(14) -> 10(24) -> 12(36)
        return 6 + 2 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc",
                    Messages.decimalFormat("#.##", 100 * (1f - visionRangeMultiplier(buffedLvl()))),
                    mindVisionRange(buffedLvl()));
        } else {
            return Messages.get(this, "typical_stats_desc",
                    Messages.decimalFormat("#.##", 100 * (1f - visionRangeMultiplier(0))),
                    mindVisionRange(0));
        }
    }

    public static float visionRangeMultiplier() {
        return visionRangeMultiplier(trinketLevel(EyeOfNewt.class));
    }

    public static float visionRangeMultiplier(int level) {
        if (level < 0) {
            return 1;
        } else {
            return 0.875f - 0.125f * level;
        }
    }

    public static int mindVisionRange() {
        return mindVisionRange(trinketLevel(EyeOfNewt.class));
    }

    public static int mindVisionRange(int level) {
        if (level < 0) {
            return 0;
        } else {
            return 2 + level;
        }
    }
}
