

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndOptions extends Window {

    protected static final int WIDTH_P = 120;
    protected static final int WIDTH_L = 144;

    protected static final int MARGIN = 2;
    protected static final int BUTTON_HEIGHT = 18;

    public WndOptions(Image icon, String title, String message, String... options) {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = 0;
        if (title != null) {
            IconTitle tfTitle = new IconTitle(icon, title);
            tfTitle.setRect(0, pos, width, 0);
            add(tfTitle);

            pos = tfTitle.bottom() + 2 * MARGIN;
        }

        layoutBody(pos, message, options);
    }

    public WndOptions(String title, String message, String... options) {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = MARGIN;
        if (title != null) {
            RenderedTextBlock tfTitle = PixelScene.renderTextBlock(title, 9);
            tfTitle.hardlight(TITLE_COLOR);
            tfTitle.setPos(MARGIN, pos);
            tfTitle.maxWidth(width - MARGIN * 2);
            add(tfTitle);

            pos = tfTitle.bottom() + 2 * MARGIN;
        }

        layoutBody(pos, message, options);
    }

    protected void layoutBody(float pos, String message, String... options) {
        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        RenderedTextBlock tfMesage = PixelScene.renderTextBlock(6);
        tfMesage.text(message, width);
        tfMesage.setPos(0, pos);
        add(tfMesage);

        pos = tfMesage.bottom() + 2 * MARGIN;

        for (int i = 0; i < options.length; i++) {
            final int index = i;
            RedButton btn = new RedButton(options[i]) {
                @Override
                protected void onClick() {
                    hide();
                    onSelect(index);
                }
            };
            if (hasIcon(i)) btn.icon(getIcon(i));
            btn.enable(enabled(i));
            add(btn);

            if (!hasInfo(i)) {
                btn.setRect(0, pos, width, BUTTON_HEIGHT);
            } else {
                btn.setRect(0, pos, width - BUTTON_HEIGHT, BUTTON_HEIGHT);
                IconButton info = new IconButton(Icons.get(Icons.INFO)) {
                    @Override
                    protected void onClick() {
                        onInfo(index);
                    }
                };
                info.setRect(width - BUTTON_HEIGHT, pos, BUTTON_HEIGHT, BUTTON_HEIGHT);
                add(info);
            }

            pos += BUTTON_HEIGHT + MARGIN;
        }

        resize(width, (int) (pos - MARGIN));
    }

    protected boolean enabled(int index) {
        return true;
    }

    protected void onSelect(int index) {
    }

    protected boolean hasInfo(int index) {
        return false;
    }

    protected void onInfo(int index) {
    }

    protected boolean hasIcon(int index) {
        return false;
    }

    protected Image getIcon(int index) {
        return null;
    }
}
