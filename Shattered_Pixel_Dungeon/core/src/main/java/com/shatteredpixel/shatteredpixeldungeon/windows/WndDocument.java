

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndDocument extends Window {

    private final ScrollingListPane list;

    public WndDocument(Document doc) {
        list = new ScrollingListPane();
        add(list);

        list.addTitle(Messages.titleCase(doc.title()));

        for (String page : doc.pageNames()) {
            boolean found = doc.isPageFound(page);
            ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(
                    doc.pageSprite(),
                    null,
                    found ? Messages.titleCase(doc.pageTitle(page)) : Messages.titleCase(Messages.get(this, "missing"))
            ) {
                @Override
                public boolean onClick(float x, float y) {
                    if (inside(x, y) && found) {
                        ShatteredPixelDungeon.scene().addToFront(new WndStory(
                                doc.pageSprite(page),
                                doc.pageTitle(page),
                                doc.pageBody(page)));
                        doc.readPage(page);
                        hardlight(Window.WHITE);
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            if (!found) {
                item.hardlight(0x999999);
                item.hardlightIcon(0x999999);
            } else if (!doc.isPageRead(page)) {
                item.hardlight(Window.TITLE_COLOR);
            }
            list.addItem(item);
        }

        resize(120, Math.min(144, (int) list.content().height()));
        list.setRect(0, 0, width, height);
    }
}
