

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

public class SupporterScene extends PixelScene {

    private static final int BTN_HEIGHT = 22;
    private static final int GAP = 2;

    @Override
    public void create() {
        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        int elementWidth = PixelScene.landscape() ? 202 : 120;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        IconTitle title = new IconTitle(Icons.GOLD.get(), Messages.get(this, "title"));
        title.setSize(200, 0);
        title.setPos(
                (w - title.reqWidth()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        SupporterMessage msg = new SupporterMessage();
        msg.setSize(elementWidth, 0);
        add(msg);

        StyledButton link = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "supporter_link")) {
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://www.patreon.com/ShatteredPixel";
                //tracking codes, so that the website knows where this pageview came from
                link += "?utm_source=shatteredpd";
                link += "&utm_medium=supporter_page";
                link += "&utm_campaign=ingame_link";
                ShatteredPixelDungeon.platform.openURI(link);
            }
        };
        link.icon(Icons.get(Icons.GOLD));
        link.textColor(Window.TITLE_COLOR);
        link.setSize(elementWidth, BTN_HEIGHT);
        add(link);

        float elementHeight = msg.height() + BTN_HEIGHT + GAP;

        float top = 16 + (h - 16 - elementHeight) / 2f;
        float left = (w - elementWidth) / 2f;

        msg.setPos(left, top);
        align(msg);

        link.setPos(left, msg.bottom() + GAP);
        align(link);
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    private static class SupporterMessage extends Component {

        NinePatch bg;
        RenderedTextBlock text;
        Image icon;

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
            add(bg);

            String message = Messages.get(SupporterScene.class, "intro");
            message += "\n\n" + Messages.get(SupporterScene.class, "patreon_msg");
            if (Messages.lang() != Languages.ENGLISH) {
                message += "\n" + Messages.get(SupporterScene.class, "patreon_english");
            }
            message += "\n\n- Evan";

            text = PixelScene.renderTextBlock(message, 6);
            add(text);

            icon = Icons.get(Icons.SHPX);
            add(icon);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;

            text.maxWidth((int) width - bg.marginHor());
            text.setPos(x + bg.marginLeft(), y + bg.marginTop() + 1);

            icon.y = text.bottom() - icon.height() + 4;
            icon.x = x + 25;

            height = (text.bottom() + 3) - y;

            height += bg.marginBottom();

            bg.size(width, height);
        }
    }
}
