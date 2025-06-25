

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndInfoItem extends Window {

    private static final float GAP = 2;

    private static final int WIDTH_MIN = 120;
    private static final int WIDTH_MAX = 220;

    //only one WndInfoItem can appear at a time
    private static WndInfoItem INSTANCE;

    public WndInfoItem(Heap heap) {

        super();

        if (INSTANCE != null) {
            INSTANCE.hide();
        }
        INSTANCE = this;

        if (heap.type == Heap.Type.HEAP) {
            fillFields(heap.peek());
        } else {
            fillFields(heap);
        }
    }

    public WndInfoItem(Item item) {
        super();

        if (INSTANCE != null) {
            INSTANCE.hide();
        }
        INSTANCE = this;

        fillFields(item);
    }

    @Override
    public void hide() {
        super.hide();
        if (INSTANCE == this) {
            INSTANCE = null;
        }
    }

    private void fillFields(Heap heap) {

        IconTitle titlebar = new IconTitle(heap);
        titlebar.color(TITLE_COLOR);

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(heap.info(), 6);

        layoutFields(titlebar, txtInfo);
    }

    private void fillFields(Item item) {

        int color = TITLE_COLOR;
        if (item.levelKnown && item.level() > 0) {
            color = ItemSlot.UPGRADED;
        } else if (item.levelKnown && item.level() < 0) {
            color = ItemSlot.DEGRADED;
        }

        IconTitle titlebar = new IconTitle(item);
        titlebar.color(color);

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(item.info(), 6);

        layoutFields(titlebar, txtInfo);
    }

    private void layoutFields(IconTitle title, RenderedTextBlock info) {
        int width = WIDTH_MIN;

        info.maxWidth(width);

        //window can go out of the screen on landscape, so widen it as appropriate
        while (PixelScene.landscape()
                && info.height() > 100
                && width < WIDTH_MAX) {
            width += 20;
            info.maxWidth(width);
        }

        //leaves some space to add the journal button in WndUseItem. This is messy I know.
        if (this instanceof WndUseItem) {
            title.setRect(0, 0, width - 16, 0);
        } else {
            title.setRect(0, 0, width, 0);
        }
        add(title);

        info.setPos(title.left(), title.bottom() + GAP);
        add(info);

        resize(width, (int) (info.bottom() + 2));
    }
}
