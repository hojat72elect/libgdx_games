

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuickBag;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Iterator;

public class Bag extends Item implements Iterable<Item> {

    public static final String AC_OPEN = "OPEN";

    {
        image = 11;

        defaultAction = AC_OPEN;

        unique = true;
    }

    public Char owner;

    public ArrayList<Item> items = new ArrayList<>();

    public int capacity() {
        return 20; // default container size
    }

    //if an item is being quick-used from the bag, the bag should take on its targeting properties
    public Item quickUseItem;

    @Override
    public int targetingPos(Hero user, int dst) {
        if (quickUseItem != null) {
            return quickUseItem.targetingPos(user, dst);
        } else {
            return super.targetingPos(user, dst);
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        quickUseItem = null;

        super.execute(hero, action);

        if (action.equals(AC_OPEN) && !items.isEmpty()) {

            GameScene.show(new WndQuickBag(this));
        }
    }

    @Override
    public boolean collect(Bag container) {

        grabItems(container);

        //if there are any quickslot placeholders that match items in this bag, assign them
        for (Item item : items) {
            Dungeon.quickslot.replacePlaceholder(item);
        }

        if (super.collect(container)) {

            owner = container.owner;

            Badges.validateAllBagsBought(this);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDetach() {
        this.owner = null;
        for (Item item : items) {
            Dungeon.quickslot.clearItem(item);
        }
        updateQuickslot();
    }

    public void grabItems() {
        if (owner != null && owner instanceof Hero && this != ((Hero) owner).belongings.backpack) {
            grabItems(((Hero) owner).belongings.backpack);
        }
    }

    public void grabItems(Bag container) {
        for (Item item : container.items.toArray(new Item[0])) {
            if (canHold(item)) {
                int slot = Dungeon.quickslot.getSlot(item);
                item.detachAll(container);
                if (!item.collect(this)) {
                    item.collect(container);
                }
                if (slot != -1) {
                    Dungeon.quickslot.setSlot(slot, item);
                }
            }
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public void clear() {
        items.clear();
    }

    public void resurrect() {
        for (Item item : items.toArray(new Item[0])) {
            if (!item.unique) items.remove(item);
        }
    }

    private static final String ITEMS = "inventory";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEMS, items);
    }

    //temp variable so that bags can load contents even with lost inventory debuff
    private boolean loading;

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        loading = true;
        for (Bundlable item : bundle.getCollection(ITEMS)) {
            if (item != null) {
                if (!((Item) item).collect(this)) {
                    //force-add the item if necessary, such as if its item category changed after an update
                    items.add((Item) item);
                }
            }
        }
        loading = false;
    }

    public boolean contains(Item item) {
        for (Item i : items) {
            if (i == item) {
                return true;
            } else if (i instanceof Bag && ((Bag) i).contains(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean canHold(Item item) {
        if (!loading && owner != null && owner.buff(LostInventory.class) != null
                && !item.keptThroughLostInventory()) {
            return false;
        }

        if (items.contains(item) || item instanceof Bag || items.size() < capacity()) {
            return true;
        } else if (item.stackable) {
            for (Item i : items) {
                if (item.isSimilar(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<Item> {

        private int index = 0;
        private Iterator<Item> nested = null;

        @Override
        public boolean hasNext() {
            if (nested != null) {
                return nested.hasNext() || index < items.size();
            } else {
                return index < items.size();
            }
        }

        @Override
        public Item next() {
            if (nested != null && nested.hasNext()) {

                return nested.next();
            } else {

                nested = null;

                Item item = items.get(index++);
                if (item instanceof Bag) {
                    nested = ((Bag) item).iterator();
                }

                return item;
            }
        }

        @Override
        public void remove() {
            if (nested != null) {
                nested.remove();
            } else {
                items.remove(index);
            }
        }
    }
}
