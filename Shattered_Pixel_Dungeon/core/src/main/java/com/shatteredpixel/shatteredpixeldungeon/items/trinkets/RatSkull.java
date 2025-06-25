

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RatSkull extends Trinket {

    {
        image = ItemSpriteSheet.RAT_SKULL;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 8(14) -> 10(24) -> 12(36)
        return 6 + 2 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", (int) (exoticChanceMultiplier(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc", (int) (exoticChanceMultiplier(0)));
        }
    }

    public static float exoticChanceMultiplier() {
        return exoticChanceMultiplier(trinketLevel(RatSkull.class));
    }

    public static float exoticChanceMultiplier(int level) {
        if (level == -1) {
            return 1f;
        } else {
            return 2f + 1f * level;
        }
    }
}
