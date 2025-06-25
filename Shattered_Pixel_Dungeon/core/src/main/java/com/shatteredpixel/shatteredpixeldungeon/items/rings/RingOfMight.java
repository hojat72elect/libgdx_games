

package com.shatteredpixel.shatteredpixeldungeon.items.rings;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfMight extends Ring {

    {
        icon = ItemSpriteSheet.Icons.RING_MIGHT;
        buffClass = Might.class;
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (super.doEquip(hero)) {
            hero.updateHT(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            hero.updateHT(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Item upgrade() {
        super.upgrade();
        updateTargetHT();
        return this;
    }

    @Override
    public void level(int value) {
        super.level(value);
        updateTargetHT();
    }

    private void updateTargetHT() {
        if (buff != null && buff.target instanceof Hero) {
            ((Hero) buff.target).updateHT(false);
        }
    }

    public String statsInfo() {
        if (isIdentified()) {
            String info = Messages.get(this, "stats",
                    soloBonus(), Messages.decimalFormat("#.##", 100f * (Math.pow(1.035, soloBuffedBonus()) - 1f)));
            if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)) {
                info += "\n\n" + Messages.get(this, "combined_stats",
                        getBonus(Dungeon.hero, Might.class), Messages.decimalFormat("#.##", 100f * (Math.pow(1.035, combinedBuffedBonus(Dungeon.hero)) - 1f)));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats", 1, Messages.decimalFormat("#.##", 3.5f));
        }
    }

    @Override
    public String upgradeStat1(int level) {
        if (cursed && cursedKnown) level = Math.min(-1, level - 3);
        return Integer.toString(level + 1);
    }

    @Override
    public String upgradeStat2(int level) {
        if (cursed && cursedKnown) level = Math.min(-1, level - 3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.035, level + 1) - 1f)) + "%";
    }

    @Override
    protected RingBuff buff() {
        return new Might();
    }

    public static int strengthBonus(Char target) {
        return getBonus(target, Might.class);
    }

    public static float HTMultiplier(Char target) {
        return (float) Math.pow(1.035, getBuffedBonus(target, Might.class));
    }

    public class Might extends RingBuff {
    }
}

