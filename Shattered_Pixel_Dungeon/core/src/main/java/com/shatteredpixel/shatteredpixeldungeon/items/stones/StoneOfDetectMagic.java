

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class StoneOfDetectMagic extends InventoryStone {

    {
        preferredBag = Belongings.Backpack.class;
        image = ItemSpriteSheet.STONE_DETECT;
    }

    @Override
    public boolean usableOnItem(Item item) {
        return (item instanceof EquipableItem || item instanceof Wand)
                && !(item instanceof MissileWeapon)
                && (!item.isIdentified() || !item.cursedKnown);
    }

    @Override
    protected void onItemSelected(Item item) {

        item.cursedKnown = true;
        useAnimation();

        boolean negativeMagic = false;
        boolean positiveMagic = false;

        negativeMagic = item.cursed;
        if (!negativeMagic) {
            if (item instanceof Weapon && ((Weapon) item).hasCurseEnchant()) {
                negativeMagic = true;
            } else if (item instanceof Armor && ((Armor) item).hasCurseGlyph()) {
                negativeMagic = true;
            }
        }

        positiveMagic = item.trueLevel() > 0;
        if (!positiveMagic) {
            if (item instanceof Weapon && ((Weapon) item).hasGoodEnchant()) {
                positiveMagic = true;
            } else if (item instanceof Armor && ((Armor) item).hasGoodGlyph()) {
                positiveMagic = true;
            }
        }

        if (!positiveMagic && !negativeMagic) {
            GLog.i(Messages.get(this, "detected_none"));
        } else if (positiveMagic && negativeMagic) {
            GLog.h(Messages.get(this, "detected_both"));
        } else if (positiveMagic) {
            GLog.p(Messages.get(this, "detected_good"));
        } else if (negativeMagic) {
            GLog.w(Messages.get(this, "detected_bad"));
        }

        if (!anonymous) {
            curItem.detach(curUser.belongings.backpack);
            Catalog.countUse(getClass());
            Talent.onRunestoneUsed(curUser, curUser.pos, getClass());
        }
    }
}
