

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;

public class PinCushion extends Buff {

    private ArrayList<MissileWeapon> items = new ArrayList<>();

    public void stick(MissileWeapon projectile) {
        for (Item item : items) {
            if (item.isSimilar(projectile)) {
                item.merge(projectile);
                if (TippedDart.lostDarts > 0) {
                    Dart d = new Dart();
                    d.quantity(TippedDart.lostDarts);
                    TippedDart.lostDarts = 0;
                    stick(d);
                }
                return;
            }
        }
        items.add(projectile);
    }

    public Item grabOne() {
        Item item = items.remove(0);
        if (items.isEmpty()) {
            detach();
        }
        return item;
    }

    public ArrayList<MissileWeapon> getStuckItems() {
        return new ArrayList<>(items);
    }

    @Override
    public void detach() {
        for (Item item : items)
            Dungeon.level.drop(item, target.pos).sprite.drop();
        super.detach();
    }

    private static final String ITEMS = "items";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(ITEMS, items);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        items = new ArrayList<>((Collection<MissileWeapon>) ((Collection<?>) bundle.getCollection(ITEMS)));
        super.restoreFromBundle(bundle);
    }

    @Override
    public int icon() {
        return BuffIndicator.PINCUSHION;
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        for (Item i : items) {
            desc += "\n" + i.title();
        }
        return desc;
    }
}
