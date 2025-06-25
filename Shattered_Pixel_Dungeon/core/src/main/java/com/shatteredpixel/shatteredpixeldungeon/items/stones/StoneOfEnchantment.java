

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class StoneOfEnchantment extends InventoryStone {

    {
        preferredBag = Belongings.Backpack.class;
        image = ItemSpriteSheet.STONE_ENCHANT;

        unique = true;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return ScrollOfEnchantment.enchantable(item);
    }

    @Override
    protected void onItemSelected(Item item) {
        if (!anonymous) {
            curItem.detach(curUser.belongings.backpack);
            Catalog.countUse(getClass());
            Talent.onRunestoneUsed(curUser, curUser.pos, getClass());
        }

        if (item instanceof Weapon) {

            ((Weapon) item).enchant();
        } else {

            ((Armor) item).inscribe();
        }

        curUser.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.1f, 5);
        Enchanting.show(curUser, item);

        if (item instanceof Weapon) {
            GLog.p(Messages.get(this, "weapon"));
        } else {
            GLog.p(Messages.get(this, "armor"));
        }

        useAnimation();
    }

    @Override
    public int value() {
        return 30 * quantity;
    }

    @Override
    public int energyVal() {
        return 5 * quantity;
    }
}
