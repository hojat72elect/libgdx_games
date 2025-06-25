

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;

public class BlandfruitBush extends Plant {

    {
        image = 12;
    }

    @Override
    public void activate(Char ch) {
        Dungeon.level.drop(new Blandfruit(), pos).sprite.drop();
    }

    //seed is never dropped
    public static class Seed extends Plant.Seed {
        {
            plantClass = BlandfruitBush.class;
        }
    }
}
