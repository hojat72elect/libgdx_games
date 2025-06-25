

package com.shatteredpixel.shatteredpixeldungeon.items.potions.brews;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class CausticBrew extends Brew {

    {
        image = ItemSpriteSheet.BREW_CAUSTIC;
    }

    @Override
    public void shatter(int cell) {

        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), 3);
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Splash.at(i, 0x000000, 5);
                Char ch = Actor.findChar(i);

                if (ch != null) {
                    Buff.affect(ch, Ooze.class).set(Ooze.DURATION);
                }
            }
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{PotionOfToxicGas.class, GooBlob.class};
            inQuantity = new int[]{1, 1};

            cost = 1;

            output = CausticBrew.class;
            outQuantity = 1;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Catalog.countUse(GooBlob.class);
            return super.brew(ingredients);
        }
    }
}
