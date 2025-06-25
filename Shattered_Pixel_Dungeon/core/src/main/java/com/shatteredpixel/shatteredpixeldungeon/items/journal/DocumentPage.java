

package com.shatteredpixel.shatteredpixeldungeon.items.journal;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public abstract class DocumentPage extends Item {

    {
        image = ItemSpriteSheet.MASTERY;
    }

    public abstract Document document();

    private String page;

    public void page(String page) {
        this.page = page;
    }

    public String page() {
        return page;
    }

    @Override
    public final boolean doPickUp(Hero hero, int pos) {
        GameScene.pickUpJournal(this, pos);
        GameScene.flashForDocument(document(), page());
        if (document() == Document.ADVENTURERS_GUIDE) {
            WndJournal.last_index = 1;
        } else if (document() == Document.ALCHEMY_GUIDE) {
            WndJournal.last_index = 2;
            WndJournal.AlchemyTab.currentPageIdx = document().pageIdx(page());
        } else if (document().isLoreDoc()) {
            WndJournal.last_index = 3;
            WndJournal.CatalogTab.currentItemIdx = 3;
        }
        document().findPage(page);
        Sample.INSTANCE.play(Assets.Sounds.ITEM);
        hero.spendAndNext(TIME_TO_PICK_UP);
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final String PAGE = "page";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PAGE, page());
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        page = bundle.getString(PAGE);
    }
}
