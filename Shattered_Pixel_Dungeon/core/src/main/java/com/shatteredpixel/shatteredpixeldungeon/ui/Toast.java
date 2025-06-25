

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.GameAction;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

public class Toast extends Component {

    private static final float MARGIN_HOR = 2;
    private static final float MARGIN_VER = 2;

    protected NinePatch bg;
    protected IconButton close;
    protected RenderedTextBlock text;

    public Toast(String text) {
        super();
        text(text);

        width = this.text.width() + close.width() + bg.marginHor() + MARGIN_HOR * 3;
        height = Math.max(this.text.height(), close.height()) + bg.marginVer() + MARGIN_VER * 2;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        bg = Chrome.get(Chrome.Type.TOAST_TR);
        add(bg);

        close = new IconButton(Icons.get(Icons.CLOSE)) {
            protected void onClick() {
                onClose();
            }

            @Override
            public GameAction keyAction() {
                return GameAction.BACK;
            }
        };
        close.setSize(close.icon.width(), close.icon.height());
        add(close);

        text = PixelScene.renderTextBlock(8);
        add(text);
    }

    @Override
    protected void layout() {
        super.layout();

        bg.x = x;
        bg.y = y;
        bg.size(width, height);

        close.setPos(
                bg.x + bg.width() - bg.marginHor() / 2f - MARGIN_HOR - close.width(),
                y + (height - close.height()) / 2f);
        PixelScene.align(close);

        text.setPos(close.left() - MARGIN_HOR - text.width(), y + (height - text.height()) / 2);
        PixelScene.align(text);
    }

    public void text(String txt) {
        text.text(txt);
    }

    protected void onClose() {
    }
}
