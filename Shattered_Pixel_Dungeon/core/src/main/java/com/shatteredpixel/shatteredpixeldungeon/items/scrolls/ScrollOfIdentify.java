

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class ScrollOfIdentify extends InventoryScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_IDENTIFY;

        bones = true;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return !item.isIdentified();
    }

    @Override
    protected void onItemSelected(Item item) {

        curUser.sprite.parent.add(new Identification(curUser.sprite.center().offset(0, -16)));

        IDItem(item);
    }

    public static void IDItem(Item item) {
        if (ShardOfOblivion.passiveIDDisabled()) {
            if (item instanceof Weapon) {
                ((Weapon) item).setIDReady();
                GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), item.name());
                return;
            } else if (item instanceof Armor) {
                ((Armor) item).setIDReady();
                GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), item.name());
                return;
            } else if (item instanceof Ring) {
                ((Ring) item).setIDReady();
                GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), item.name());
                return;
            } else if (item instanceof Wand) {
                ((Wand) item).setIDReady();
                GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), item.name());
                return;
            }
        }

        item.identify();
        GLog.i(Messages.get(ScrollOfIdentify.class, "it_is", item.title()));
        Badges.validateItemLevelAquired(item);
    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
