

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ExoticCrystals extends Trinket {

    {
        image = ItemSpriteSheet.EXOTIC_CRYSTALS;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 8(14) -> 10(24) -> 12(36)
        return 6 + 2 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", Messages.decimalFormat("#.##", 100 * consumableExoticChance(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc", Messages.decimalFormat("#.##", 100 * consumableExoticChance(0)));
        }
    }

    public static float consumableExoticChance() {
        return consumableExoticChance(trinketLevel(ExoticCrystals.class));
    }

    public static float consumableExoticChance(int level) {
        if (level == -1) {
            return 0f;
        } else {
            return 0.125f + 0.125f * level;
        }
    }
}
