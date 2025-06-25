

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TrapMechanism extends Trinket {

    {
        image = ItemSpriteSheet.TRAP_MECHANISM;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 8(14) -> 10(24) -> 12(36)
        return 6 + 2 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", (int) (100 * overrideNormalLevelChance(buffedLvl())), (int) (100 * revealHiddenTrapChance(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc", (int) (100 * overrideNormalLevelChance(0)), (int) (100 * revealHiddenTrapChance(0)));
        }
    }

    public static float overrideNormalLevelChance() {
        return overrideNormalLevelChance(trinketLevel(TrapMechanism.class));
    }

    public static float overrideNormalLevelChance(int level) {
        if (level == -1) {
            return 0f;
        } else {
            return 0.25f + 0.25f * level;
        }
    }

    public static float revealHiddenTrapChance() {
        return revealHiddenTrapChance(trinketLevel(TrapMechanism.class));
    }

    public static float revealHiddenTrapChance(int level) {
        if (level == -1) {
            return 0f;
        } else {
            return 0.1f + 0.1f * level;
        }
    }

    //true for traps, false for chasm
    //ensures a little consistency of RNG
    private final ArrayList<Boolean> levelFeels = new ArrayList<>();
    private int shuffles = 0;

    public static Level.Feeling getNextFeeling() {
        TrapMechanism mech = Dungeon.hero.belongings.getItem(TrapMechanism.class);
        if (mech == null) {
            return Level.Feeling.NONE;
        }
        if (mech.levelFeels.isEmpty()) {
            Random.pushGenerator(Dungeon.seed + 1);
            mech.levelFeels.add(true);
            mech.levelFeels.add(true);
            mech.levelFeels.add(true);
            mech.levelFeels.add(false);
            mech.levelFeels.add(false);
            mech.levelFeels.add(false);
            for (int i = 0; i <= mech.shuffles; i++) {
                Random.shuffle(mech.levelFeels);
            }
            mech.shuffles++;
            Random.popGenerator();
        }

        return mech.levelFeels.remove(0) ? Level.Feeling.TRAPS : Level.Feeling.CHASM;
    }

    private static final String FEELS = "feels";
    private static final String SHUFFLES = "shuffles";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (!levelFeels.isEmpty()) {
            boolean[] storeFeels = new boolean[levelFeels.size()];
            for (int i = 0; i < storeFeels.length; i++) {
                storeFeels[i] = levelFeels.get(i);
            }
            bundle.put(FEELS, storeFeels);
        }
        bundle.put(SHUFFLES, shuffles);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        levelFeels.clear();
        if (bundle.contains(FEELS)) {
            for (boolean b : bundle.getBooleanArray(FEELS)) {
                levelFeels.add(b);
            }
        }
        shuffles = bundle.getInt(SHUFFLES);
    }
}
