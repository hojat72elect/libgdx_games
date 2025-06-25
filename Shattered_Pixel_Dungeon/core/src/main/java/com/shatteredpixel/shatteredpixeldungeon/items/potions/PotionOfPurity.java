

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfPurity extends Potion {

    private static final int DISTANCE = 3;

    private static ArrayList<Class> affectedBlobs;

    {
        icon = ItemSpriteSheet.Icons.POTION_PURITY;

        affectedBlobs = new ArrayList<>(new BlobImmunity().immunities());
    }

    @Override
    public void shatter(int cell) {

        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), DISTANCE);

        ArrayList<Blob> blobs = new ArrayList<>();
        for (Class c : affectedBlobs) {
            Blob b = Dungeon.level.blobs.get(c);
            if (b != null && b.volume > 0) {
                blobs.add(b);
            }
        }

        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {

                for (Blob blob : blobs) {
                    blob.clear(i);
                }

                if (Dungeon.level.heroFOV[i]) {
                    CellEmitter.get(i).burst(Speck.factory(Speck.DISCOVER), 2);
                }
            }
        }


        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);

            identify();
            GLog.i(Messages.get(this, "freshness"));
        }
    }

    @Override
    public void apply(Hero hero) {
        GLog.w(Messages.get(this, "protected"));
        Buff.prolong(hero, BlobImmunity.class, BlobImmunity.DURATION);
        SpellSprite.show(hero, SpellSprite.PURITY);
        identify();
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }
}
