

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DimensionalSundial extends Trinket {

    {
        image = ItemSpriteSheet.SUNDIAL;
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
                    (int) (100 * (1f - enemySpawnMultiplierDaytime(buffedLvl()))),
                    (int) (100 * (enemySpawnMultiplierNighttime(buffedLvl()) - 1f)));
        } else {
            return Messages.get(this, "typical_stats_desc",
                    (int) (100 * (1f - enemySpawnMultiplierDaytime(0))),
                    (int) (100 * (enemySpawnMultiplierNighttime(0) - 1f)));
        }
    }

    public static boolean sundialWarned = false;

    public static float spawnMultiplierAtCurrentTime() {
        if (trinketLevel(DimensionalSundial.class) != -1) {
            Calendar cal = GregorianCalendar.getInstance();
            if (cal.get(Calendar.HOUR_OF_DAY) >= 20 || cal.get(Calendar.HOUR_OF_DAY) <= 7) {
                if (!sundialWarned) {
                    GLog.w(Messages.get(DimensionalSundial.class, "warning"));
                    sundialWarned = true;
                }
                return enemySpawnMultiplierNighttime();
            } else {
                return enemySpawnMultiplierDaytime();
            }
        } else {
            return 1f;
        }
    }

    public static float enemySpawnMultiplierDaytime() {
        return enemySpawnMultiplierDaytime(trinketLevel(DimensionalSundial.class));
    }

    public static float enemySpawnMultiplierDaytime(int level) {
        if (level == -1) {
            return 1f;
        } else {
            return 0.95f - 0.05f * level;
        }
    }

    public static float enemySpawnMultiplierNighttime() {
        return enemySpawnMultiplierNighttime(trinketLevel(DimensionalSundial.class));
    }

    public static float enemySpawnMultiplierNighttime(int level) {
        if (level == -1) {
            return 1f;
        } else {
            return 1.25f + 0.25f * level;
        }
    }
}
