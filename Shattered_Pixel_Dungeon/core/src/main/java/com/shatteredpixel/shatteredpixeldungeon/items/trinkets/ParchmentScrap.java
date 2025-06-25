

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ParchmentScrap extends Trinket {

    {
        image = ItemSpriteSheet.PARCHMENT_SCRAP;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 10(16) -> 15(31) -> 20(51)
        return 10 + 5 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", (int) enchantChanceMultiplier(buffedLvl()), Messages.decimalFormat("#.##", curseChanceMultiplier(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc", (int) enchantChanceMultiplier(0), Messages.decimalFormat("#.##", curseChanceMultiplier(0)));
        }
    }

    public static float enchantChanceMultiplier() {
        return enchantChanceMultiplier(trinketLevel(ParchmentScrap.class));
    }

    public static float enchantChanceMultiplier(int level) {
        switch (level) {
            default:
                return 1;
            case 0:
                return 2;
            case 1:
                return 4;
            case 2:
                return 7;
            case 3:
                return 10;
        }
    }

    public static float curseChanceMultiplier() {
        return curseChanceMultiplier(trinketLevel(ParchmentScrap.class));
    }

    public static float curseChanceMultiplier(int level) {
        switch (level) {
            default:
                return 1;
            case 0:
                return 1.5f;
            case 1:
                return 2f;
            case 2:
                return 1f;
            case 3:
                return 0f;
        }
    }
}
