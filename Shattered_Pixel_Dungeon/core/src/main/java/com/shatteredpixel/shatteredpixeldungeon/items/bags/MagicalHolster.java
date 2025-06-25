

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MagicalHolster extends Bag {

    {
        image = ItemSpriteSheet.HOLSTER;
    }

    public static final float HOLSTER_SCALE_FACTOR = 0.85f;
    public static final float HOLSTER_DURABILITY_FACTOR = 1.2f;

    @Override
    public boolean canHold(Item item) {
        if (item instanceof Wand || item instanceof MissileWeapon || item instanceof Bomb) {
            return super.canHold(item);
        } else {
            return false;
        }
    }

    public int capacity() {
        return 19;
    }

    @Override
    public boolean collect(Bag container) {
        if (super.collect(container)) {
            if (owner != null) {
                for (Item item : items) {
                    if (item instanceof Wand) {
                        ((Wand) item).charge(owner, HOLSTER_SCALE_FACTOR);
                    } else if (item instanceof MissileWeapon) {
                        ((MissileWeapon) item).holster = true;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (Item item : items) {
            if (item instanceof Wand) {
                ((Wand) item).stopCharging();
            } else if (item instanceof MissileWeapon) {
                ((MissileWeapon) item).holster = false;
            }
        }
    }

    @Override
    public int value() {
        return 60;
    }
}
