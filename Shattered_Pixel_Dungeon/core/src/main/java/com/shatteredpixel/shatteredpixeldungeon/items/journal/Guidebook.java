

package com.shatteredpixel.shatteredpixeldungeon.items.journal;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.input.ControllerHandler;
import com.watabou.input.KeyBindings;
import com.watabou.noosa.audio.Sample;

public class Guidebook extends Item {

    {
        image = ItemSpriteSheet.MASTERY;
    }

    @Override
    public final boolean doPickUp(Hero hero, int pos) {
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_INTRO);
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_EXAMINING);
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_SURPRISE_ATKS);
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_IDING);
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_FOOD);
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_ALCHEMY);
        Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_DIEING);

        GameScene.pickUpJournal(this, pos);
        //we do this here so the pickup message appears before the tutorial text
        GameLog.wipe();
        GLog.i(Messages.capitalize(Messages.get(Hero.class, "you_now_have", name())));
        if (SPDSettings.interfaceSize() == 0) {
            GLog.p(Messages.get(GameScene.class, "tutorial_guidebook_mobile"));
        } else {
            GLog.p(Messages.get(GameScene.class, "tutorial_guidebook_desktop", KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(SPDAction.JOURNAL, ControllerHandler.isControllerConnected()))));
        }
        GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_INTRO);
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
}
