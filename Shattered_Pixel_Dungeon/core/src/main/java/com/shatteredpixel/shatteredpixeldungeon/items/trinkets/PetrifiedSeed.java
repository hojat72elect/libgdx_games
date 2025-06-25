

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PetrifiedSeed extends Trinket {

    {
        image = ItemSpriteSheet.PETRIFIED_SEED;
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
                    Messages.decimalFormat("#.##", 100 * stoneInsteadOfSeedChance(buffedLvl())),
                    Messages.decimalFormat("#.##", 100 * (grassLootMultiplier(buffedLvl()) - 1f)));
        } else {
            return Messages.get(this, "typical_stats_desc",
                    Messages.decimalFormat("#.##", 100 * stoneInsteadOfSeedChance(0)),
                    Messages.decimalFormat("#.##", 100 * (grassLootMultiplier(0) - 1f)));
        }
    }

    public static float grassLootMultiplier() {
        return grassLootMultiplier(trinketLevel(PetrifiedSeed.class));
    }

    public static float grassLootMultiplier(int level) {
        if (level <= 0) {
            return 1f;
        } else {
            return 1f + .25f * level / 3f;
        }
    }

    public static float stoneInsteadOfSeedChance() {
        return stoneInsteadOfSeedChance(trinketLevel(PetrifiedSeed.class));
    }

    //when accounting for boosts, we effectively get:
    //stones: 25/50/75/100%
    //seeds:  75/58/38/25%
    public static float stoneInsteadOfSeedChance(int level) {
        switch (level) {
            default:
                return 0;
            case 0:
                return 0.25f;
            case 1:
                return 0.46f;
            case 2:
                return 0.65f;
            case 3:
                return 0.8f;
        }
    }
}
