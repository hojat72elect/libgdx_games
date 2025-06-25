

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ChangeInfo extends Component {

    protected ColorBlock line;

    private final RenderedTextBlock title;
    public boolean major;

    private RenderedTextBlock text;

    private final ArrayList<ChangeButton> buttons = new ArrayList<>();

    public ChangeInfo(String title, boolean majorTitle, String text) {
        super();

        if (majorTitle) {
            this.title = PixelScene.renderTextBlock(title, 9);
            line = new ColorBlock(1, 1, 0xFF222222);
            add(line);
        } else {
            this.title = PixelScene.renderTextBlock(title, 6);
            line = new ColorBlock(1, 1, 0xFF333333);
            add(line);
        }
        major = majorTitle;

        add(this.title);

        if (text != null && !text.equals("")) {
            this.text = PixelScene.renderTextBlock(text, 6);
            add(this.text);
        }
    }

    public void hardlight(int color) {
        title.hardlight(color);
        if (text != null) {
            text.hardlight(color);
        }
    }

    public void addButton(ChangeButton button) {
        buttons.add(button);
        add(button);

        button.setSize(16, 16);
        layout();
    }

    public boolean onClick(float x, float y) {
        for (ChangeButton button : buttons) {
            if (button.inside(x, y)) {
                button.onClick();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void layout() {
        float posY = this.y + 3;
        if (major) posY += 2;

        title.setPos(
                x + (width - title.width()) / 2f,
                posY
        );
        PixelScene.align(title);
        posY += title.height() + 2;

        if (text != null) {
            text.maxWidth((int) width());
            text.setPos(x, posY);
            posY += text.height();
        }

        float posX = x;
        float tallest = 0;
        for (ChangeButton change : buttons) {

            if (posX + change.width() >= right()) {
                posX = x;
                posY += tallest;
                tallest = 0;
            }

            //centers
            if (posX == x) {
                float offset = width;
                for (ChangeButton b : buttons) {
                    offset -= b.width();
                    if (offset <= 0) {
                        offset += b.width();
                        break;
                    }
                }
                posX += offset / 2f;
            }

            change.setPos(posX, posY);
            posX += change.width();
            if (tallest < change.height()) {
                tallest = change.height();
            }
        }
        posY += tallest + 2;

        height = posY - this.y;

        if (major) {
            line.size(width(), 1);
            line.x = x;
            line.y = y + 2;
        } else if (x == 0) {
            line.size(1, height());
            line.x = width;
            line.y = y;
        } else {
            line.size(1, height());
            line.x = x;
            line.y = y;
        }
    }
}