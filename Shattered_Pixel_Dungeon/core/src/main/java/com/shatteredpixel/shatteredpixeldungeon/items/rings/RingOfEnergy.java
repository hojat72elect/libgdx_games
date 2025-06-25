

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfEnergy extends Ring {

    {
        icon = ItemSpriteSheet.Icons.RING_ENERGY;
        buffClass = Energy.class;
    }

    public String statsInfo() {
        if (isIdentified()) {
            String info = Messages.get(this, "stats",
                    Messages.decimalFormat("#.##", 100f * (Math.pow(1.175f, soloBuffedBonus()) - 1f)));
            if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)) {
                info += "\n\n" + Messages.get(this, "combined_stats",
                        Messages.decimalFormat("#.##", 100f * (Math.pow(1.175f, combinedBuffedBonus(Dungeon.hero)) - 1f)));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats",
                    Messages.decimalFormat("#.##", 17.5f));
        }
    }

    public String upgradeStat1(int level) {
        if (cursed && cursedKnown) level = Math.min(-1, level - 3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.175f, level + 1) - 1f)) + "%";
    }

    @Override
    protected RingBuff buff() {
        return new Energy();
    }

    public static float wandChargeMultiplier(Char target) {
        float bonus = (float) Math.pow(1.175, getBuffedBonus(target, Energy.class));

        if (target instanceof Hero && ((Hero) target).heroClass != HeroClass.CLERIC && ((Hero) target).hasTalent(Talent.LIGHT_READING)) {
            bonus *= 1f + (0.2f * ((Hero) target).pointsInTalent(Talent.LIGHT_READING) / 3f);
        }

        return bonus;
    }

    public static float artifactChargeMultiplier(Char target) {
        float bonus = (float) Math.pow(1.175, getBuffedBonus(target, Energy.class));

        if (target instanceof Hero && ((Hero) target).heroClass != HeroClass.ROGUE && ((Hero) target).hasTalent(Talent.LIGHT_CLOAK)) {
            bonus *= 1f + (0.2f * ((Hero) target).pointsInTalent(Talent.LIGHT_CLOAK) / 3f);
        }

        return bonus;
    }

    public static float armorChargeMultiplier(Char target) {
        return (float) Math.pow(1.175, getBuffedBonus(target, Energy.class));
    }

    public class Energy extends RingBuff {
    }
}
