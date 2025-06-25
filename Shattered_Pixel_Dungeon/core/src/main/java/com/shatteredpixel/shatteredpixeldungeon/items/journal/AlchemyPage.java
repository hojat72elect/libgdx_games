

package com.shatteredpixel.shatteredpixeldungeon.items.journal;

import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AlchemyPage extends DocumentPage {

    {
        image = ItemSpriteSheet.ALCH_PAGE;
    }

    @Override
    public Document document() {
        return Document.ALCHEMY_GUIDE;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", document().pageTitle(page()));
    }
}
