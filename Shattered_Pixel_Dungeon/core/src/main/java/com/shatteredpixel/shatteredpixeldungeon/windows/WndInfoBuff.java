

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndInfoBuff extends Window {

    private static final float GAP = 2;

    private static final int WIDTH = 120;

    public WndInfoBuff(Buff buff) {
        super();

        IconTitle titlebar = new IconTitle();

        Image buffIcon = new BuffIcon(buff, true);

        titlebar.icon(buffIcon);
        titlebar.label(Messages.titleCase(buff.name()), Window.TITLE_COLOR);
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(buff.desc(), 6);
        txtInfo.maxWidth(WIDTH);
        txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
        add(txtInfo);

        resize(WIDTH, (int) txtInfo.bottom() + 2);
    }
}
