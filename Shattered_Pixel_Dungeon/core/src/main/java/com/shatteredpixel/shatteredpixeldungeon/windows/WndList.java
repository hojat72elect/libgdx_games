

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndList extends Window {

    private static final int WIDTH = 120;
    private static final int MARGIN = 4;
    private static final int GAP = 4;

    public WndList(String[] items) {

        super();

        float pos = MARGIN;
        float dotWidth = 0;
        float maxWidth = 0;

        for (int i = 0; i < items.length; i++) {

            if (i > 0) {
                pos += GAP;
            }

            RenderedTextBlock item = PixelScene.renderTextBlock("-" + items[i], 6);
            item.setPos(MARGIN, pos);
            item.maxWidth(WIDTH - MARGIN * 2);
            add(item);

            pos += item.height();
            float w = item.width();
            if (w > maxWidth) {
                maxWidth = w;
            }
        }

        resize((int) (maxWidth + dotWidth + MARGIN * 2), (int) (pos + MARGIN));
    }
}
