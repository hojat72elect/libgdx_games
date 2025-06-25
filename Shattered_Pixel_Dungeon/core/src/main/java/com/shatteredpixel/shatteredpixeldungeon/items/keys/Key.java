

package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public abstract class Key extends Item {

    public static final float TIME_TO_UNLOCK = 1f;

    {
        stackable = true;
        unique = true;
    }

    //TODO currently keys can only appear on branch = 0, add branch support here if that changes
    public int depth;

    @Override
    public boolean isSimilar(Item item) {
        return super.isSimilar(item) && ((Key) item).depth == depth;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        Catalog.setSeen(getClass());
        Statistics.itemTypesDiscovered.add(getClass());
        GameScene.pickUpJournal(this, pos);
        WndJournal.last_index = 0;
        Notes.add(this);
        Sample.INSTANCE.play(Assets.Sounds.ITEM);
        hero.spendAndNext(TIME_TO_PICK_UP);
        GameScene.updateKeyDisplay();
        return true;
    }

    private static final String DEPTH = "depth";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEPTH, depth);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        depth = bundle.getInt(DEPTH);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
