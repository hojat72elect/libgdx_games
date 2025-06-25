

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MimicTooth extends Trinket {

    {
        image = ItemSpriteSheet.MIMIC_TOOTH;
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
                    Messages.decimalFormat("#.##", mimicChanceMultiplier(buffedLvl())),
                    Messages.decimalFormat("#.##", 100 * ebonyMimicChance(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc",
                    Messages.decimalFormat("#.##", mimicChanceMultiplier(0)),
                    Messages.decimalFormat("#.##", 100 * ebonyMimicChance(0)));
        }
    }

    public static float mimicChanceMultiplier() {
        return mimicChanceMultiplier(trinketLevel(MimicTooth.class));
    }

    public static float mimicChanceMultiplier(int level) {
        if (level == -1) {
            return 1f;
        } else {
            return 1.5f + 0.5f * level;
        }
    }

    public static boolean stealthyMimics() {
        return trinketLevel(MimicTooth.class) >= 0;
    }

    public static float ebonyMimicChance() {
        return ebonyMimicChance(trinketLevel(MimicTooth.class));
    }

    public static float ebonyMimicChance(int level) {
        if (level >= 0) {
            return 0.125f + 0.125f * level;
        } else {
            return 0;
        }
    }
}
