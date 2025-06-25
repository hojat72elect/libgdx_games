

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class FerretTuft extends Trinket {

    {
        image = ItemSpriteSheet.FERRET_TUFT;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 8(14) -> 10(24) -> 12(36)
        return 6 + 2 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", Messages.decimalFormat("#.##", 100 * (evasionMultiplier(buffedLvl()) - 1f)));
        } else {
            return Messages.get(this, "typical_stats_desc", Messages.decimalFormat("#.##", 100 * (evasionMultiplier(0) - 1f)));
        }
    }

    public static float evasionMultiplier() {
        return evasionMultiplier(trinketLevel(FerretTuft.class));
    }

    public static float evasionMultiplier(int level) {
        if (level <= -1) {
            return 1;
        } else {
            return 1 + 0.125f * (level + 1);
        }
    }
}
