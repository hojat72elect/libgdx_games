

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SupporterScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndSupportPrompt extends Window {

    protected static final int WIDTH_P = 120;
    protected static final int WIDTH_L = 200;

    public WndSupportPrompt() {

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        IconTitle title = new IconTitle(Icons.get(Icons.SHPX), Messages.get(WndSupportPrompt.class, "title"));
        title.setRect(0, 0, width, 0);
        add(title);

        String message = Messages.get(WndSupportPrompt.class, "intro");
        message += "\n\n" + Messages.get(SupporterScene.class, "patreon_msg");
        if (Messages.lang() != Languages.ENGLISH) {
            message += "\n" + Messages.get(SupporterScene.class, "patreon_english");
        }
        message += "\n- Evan";

        RenderedTextBlock text = PixelScene.renderTextBlock(6);
        text.text(message, width);
        text.setPos(title.left(), title.bottom() + 4);
        add(text);

        RedButton link = new RedButton(Messages.get(SupporterScene.class, "supporter_link")) {
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://www.patreon.com/ShatteredPixel";
                //tracking codes, so that the website knows where this pageview came from
                link += "?utm_source=shatteredpd";
                link += "&utm_medium=supporter_prompt";
                link += "&utm_campaign=ingame_link";
                ShatteredPixelDungeon.platform.openURI(link);
                SPDSettings.supportNagged(true);
                WndSupportPrompt.super.hide();
            }
        };
        link.setRect(0, text.bottom() + 4, width, 18);
        add(link);

        RedButton close = new RedButton(Messages.get(this, "close")) {
            @Override
            protected void onClick() {
                super.onClick();
                SPDSettings.supportNagged(true);
                WndSupportPrompt.super.hide();
            }
        };
        close.setRect(0, link.bottom() + 2, width, 18);
        add(close);

        resize(width, (int) close.bottom());
    }

    @Override
    public void hide() {
        //do nothing, have to close via the close button
    }
}
