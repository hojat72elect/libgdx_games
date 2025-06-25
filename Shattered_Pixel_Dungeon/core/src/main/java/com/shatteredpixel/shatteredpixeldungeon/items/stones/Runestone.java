

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public abstract class Runestone extends Item {

    {
        stackable = true;
        defaultAction = AC_THROW;
    }

    //anonymous stones don't count as consumed, do not drop, etc.
    //useful for stones which are only spawned for their effects
    protected boolean anonymous = false;

    public void anonymize() {
        image = ItemSpriteSheet.STONE_HOLDER;
        anonymous = true;
    }

    @Override
    protected void onThrow(int cell) {
        ///inventory stones are thrown like normal items, other stones don't trigger when thrown into pits
        if (this instanceof InventoryStone ||
                Dungeon.hero.buff(MagicImmune.class) != null ||
                (Dungeon.level.pit[cell] && Actor.findChar(cell) == null)) {
            if (!anonymous) super.onThrow(cell);
        } else {
            if (!anonymous) {
                Catalog.countUse(getClass());
                Talent.onRunestoneUsed(curUser, cell, getClass());
            }
            activate(cell);
            if (Actor.findChar(cell) == null) Dungeon.level.pressCell(cell);
            Invisibility.dispel();
        }
    }

    protected abstract void activate(int cell);

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 15 * quantity;
    }

    @Override
    public int energyVal() {
        return 3 * quantity;
    }

    public static class PlaceHolder extends Runestone {

        {
            image = ItemSpriteSheet.STONE_HOLDER;
        }

        @Override
        protected void activate(int cell) {
            //does nothing
        }

        @Override
        public boolean isSimilar(Item item) {
            return item instanceof Runestone;
        }

        @Override
        public String info() {
            return "";
        }
    }
}
