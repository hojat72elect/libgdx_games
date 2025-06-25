

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Callback;

public class WndInfoTalent extends Window {

    private static final float GAP = 2;

    private static final int WIDTH_MIN = 120;
    private static final int WIDTH_MAX = 220;

    public WndInfoTalent(Talent talent, int points, TalentButtonCallback buttonCallback) {
        super();

        int width = WIDTH_MIN;

        IconTitle titlebar = new IconTitle();

        titlebar.icon(new TalentIcon(talent));
        String title = Messages.titleCase(talent.title());
        if (points > 0) {
            title += " +" + points;
        }
        titlebar.label(title, Window.TITLE_COLOR);
        titlebar.setRect(0, 0, width, 0);
        add(titlebar);

        boolean metaDesc = (buttonCallback != null && buttonCallback.metamorphDesc()) ||
                (Dungeon.hero != null && Dungeon.hero.metamorphedTalents.containsValue(talent));

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(talent.desc(metaDesc), 6);
        txtInfo.maxWidth(width);
        txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
        add(txtInfo);

        while (PixelScene.landscape()
                && txtInfo.height() > 120
                && width < WIDTH_MAX) {
            width += 20;
            txtInfo.maxWidth(width);
        }
        titlebar.setRect(0, 0, width, 0);
        resize(width, (int) (txtInfo.bottom() + GAP));

        if (buttonCallback != null) {
            RedButton button = new RedButton(buttonCallback.prompt()) {
                @Override
                protected void onClick() {
                    super.onClick();
                    hide();
                    buttonCallback.call();
                }
            };
            button.icon(Icons.get(Icons.TALENT));
            button.setRect(0, txtInfo.bottom() + 2 * GAP, width, 18);
            add(button);
            resize(width, (int) button.bottom() + 1);
        }
    }

    public static abstract class TalentButtonCallback implements Callback {

        public abstract String prompt();

        public boolean metamorphDesc() {
            return false;
        }
    }
}
