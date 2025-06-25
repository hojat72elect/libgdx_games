

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MossyClump extends Trinket {

    {
        image = ItemSpriteSheet.MOSSY_CLUMP;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 10(16) -> 15(31) -> 20(51)
        return 10 + 5 * level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", (int) (100 * overrideNormalLevelChance(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc", (int) (100 * overrideNormalLevelChance(0)));
        }
    }

    public static float overrideNormalLevelChance() {
        return overrideNormalLevelChance(trinketLevel(MossyClump.class));
    }

    public static float overrideNormalLevelChance(int level) {
        if (level == -1) {
            return 0f;
        } else {
            return 0.25f + 0.25f * level;
        }
    }

    //true for grass, false for water
    //ensures a little consistency of RNG
    private final ArrayList<Boolean> levelFeels = new ArrayList<>();
    private int shuffles = 0;

    public static Level.Feeling getNextFeeling() {
        MossyClump clump = Dungeon.hero.belongings.getItem(MossyClump.class);
        if (clump == null) {
            return Level.Feeling.NONE;
        }
        if (clump.levelFeels.isEmpty()) {
            Random.pushGenerator(Dungeon.seed + 1);
            clump.levelFeels.add(true);
            clump.levelFeels.add(true);
            clump.levelFeels.add(false);
            clump.levelFeels.add(false);
            clump.levelFeels.add(false);
            clump.levelFeels.add(false);
            for (int i = 0; i <= clump.shuffles; i++) {
                Random.shuffle(clump.levelFeels);
            }
            clump.shuffles++;
            Random.popGenerator();
        }

        return clump.levelFeels.remove(0) ? Level.Feeling.GRASS : Level.Feeling.WATER;
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
