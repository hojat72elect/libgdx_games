

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

    {
        image = ItemSpriteSheet.HOLDER;
    }

    @Override
    public boolean canHold(Item item) {
        if (item instanceof Scroll || item instanceof Spell
                || item instanceof ArcaneResin || item instanceof Stylus) {
            return super.canHold(item);
        } else {
            return false;
        }
    }

    public int capacity() {
        return 19;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (Item item : items) {
            if (item instanceof BeaconOfReturning) {
                Notes.remove(Notes.Landmark.BEACON_LOCATION, ((BeaconOfReturning) item).returnDepth);
                ((BeaconOfReturning) item).returnDepth = -1;
            }
        }
    }

    @Override
    public int value() {
        return 40;
    }
}
