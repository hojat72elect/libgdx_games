

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndMessage extends Window {

    private static final int WIDTH_MIN = 120;
    private static final int WIDTH_MAX = 220;
    private static final int MARGIN = 4;

    public WndMessage(String text) {

        super();

        int width = WIDTH_MIN;

        RenderedTextBlock info = PixelScene.renderTextBlock(text, 6);
        info.maxWidth(width - MARGIN * 2);
        info.setPos(MARGIN, MARGIN);
        add(info);

        while (PixelScene.landscape()
                && info.height() > 120
                && width < WIDTH_MAX) {
            width += 20;
            info.maxWidth(width - MARGIN * 2);
        }

        resize(
                (int) info.width() + MARGIN * 2,
                (int) info.height() + MARGIN * 2);
    }
}
