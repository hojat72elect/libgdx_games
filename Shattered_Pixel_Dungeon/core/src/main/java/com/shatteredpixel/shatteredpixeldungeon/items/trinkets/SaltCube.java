

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SaltCube extends Trinket {

    {
        image = ItemSpriteSheet.SALT_CUBE;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 8(14) -> 10(24) -> 12(36)
        return 6 + 2 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this,
                    "stats_desc",
                    Messages.decimalFormat("#.##", 100 * ((1f / hungerGainMultiplier(buffedLvl())) - 1f)),
                    Messages.decimalFormat("#.##", 100 * (1f - healthRegenMultiplier(buffedLvl()))));
        } else {
            return Messages.get(this,
                    "typical_stats_desc",
                    Messages.decimalFormat("#.##", 100 * ((1f / hungerGainMultiplier(0)) - 1f)),
                    Messages.decimalFormat("#.##", 100 * (1f - healthRegenMultiplier(0))));
        }
    }

    public static float hungerGainMultiplier() {
        return hungerGainMultiplier(trinketLevel(SaltCube.class));
    }

    public static float hungerGainMultiplier(int level) {
        if (level == -1) {
            return 1;
        } else {
            return 1f / (1f + 0.25f * (level + 1));
        }
    }

    public static float healthRegenMultiplier() {
        return healthRegenMultiplier(trinketLevel(SaltCube.class));
    }

    public static float healthRegenMultiplier(int level) {
        switch (level) {
            case -1:
            default:
                return 1;
            case 0:
                return 0.84f;
            case 1:
                return 0.73f;
            case 2:
                return 0.66f;
            case 3:
                return 0.6f;
        }
    }
}
