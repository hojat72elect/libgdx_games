

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class VialOfBlood extends Trinket {

    {
        image = ItemSpriteSheet.BLOOD_VIAL;
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
                    Messages.decimalFormat("#.##", 100 * (totalHealMultiplier(buffedLvl()) - 1f)),
                    Integer.toString(maxHealPerTurn(buffedLvl())));
        } else {
            return Messages.get(this,
                    "typical_stats_desc",
                    Messages.decimalFormat("#.##", 100 * (totalHealMultiplier(0) - 1f)),
                    Integer.toString(maxHealPerTurn(0)));
        }
    }

    public static boolean delayBurstHealing() {
        return trinketLevel(VialOfBlood.class) != -1;
    }

    public static float totalHealMultiplier() {
        return totalHealMultiplier(trinketLevel(VialOfBlood.class));
    }

    public static float totalHealMultiplier(int level) {
        if (level == -1) {
            return 1;
        } else {
            return 1f + 0.125f * (level + 1);
        }
    }

    public static int maxHealPerTurn() {
        return maxHealPerTurn(trinketLevel(VialOfBlood.class));
    }

    public static int maxHealPerTurn(int level) {
        int maxHP = Dungeon.hero == null ? 20 : Dungeon.hero.HT;
        if (level == -1) {
            return maxHP;
        } else {
            switch (level) {
                case 0:
                default:
                    return 4 + Math.round(0.15f * maxHP);
                case 1:
                    return 3 + Math.round(0.10f * maxHP);
                case 2:
                    return 2 + Math.round(0.07f * maxHP);
                case 3:
                    return 1 + Math.round(0.05f * maxHP);
            }
        }
    }
}
