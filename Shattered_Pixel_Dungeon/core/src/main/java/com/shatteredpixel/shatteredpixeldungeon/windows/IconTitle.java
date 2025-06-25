

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class IconTitle extends Component {

    private static final float FONT_SIZE = 9;

    private static final float GAP = 2;

    protected Image imIcon;
    protected RenderedTextBlock tfLabel;
    protected HealthBar health;

    private float healthLvl = Float.NaN;

    public IconTitle() {
        super();
    }

    public IconTitle(Item item) {
        ItemSprite icon = new ItemSprite();
        icon(icon);
        label(Messages.titleCase(item.title()));
        icon.view(item);
        layout();
    }

    public IconTitle(Heap heap) {
        ItemSprite icon = new ItemSprite();
        icon(icon);
        label(Messages.titleCase(heap.title()));
        icon.view(heap);
        layout();
    }

    public IconTitle(Image icon, String label) {
        icon(icon);
        label(label);
        layout();
    }

    @Override
    protected void createChildren() {
        imIcon = new Image();
        add(imIcon);

        tfLabel = PixelScene.renderTextBlock((int) FONT_SIZE);
        tfLabel.hardlight(Window.TITLE_COLOR);
        tfLabel.setHightlighting(false);
        add(tfLabel);

        health = new HealthBar();
        add(health);
    }

    @Override
    protected void layout() {

        health.visible = !Float.isNaN(healthLvl);

        imIcon.x = x + (Math.max(0, 8 - imIcon.width() / 2));
        imIcon.y = y + (Math.max(0, 8 - imIcon.height() / 2));
        PixelScene.align(imIcon);

        int imWidth = (int) Math.max(imIcon.width(), 16);
        int imHeight = (int) Math.max(imIcon.height(), 16);

        tfLabel.maxWidth((int) (width - (imWidth + GAP)));
        tfLabel.setPos(x + imWidth + GAP,
                imHeight > tfLabel.height() ? y + (imHeight - tfLabel.height()) / 2 : y);
        PixelScene.align(tfLabel);

        if (health.visible) {
            health.setRect(tfLabel.left(), tfLabel.bottom(), tfLabel.maxWidth(), 0);
            height = Math.max(imHeight, health.bottom());
        } else {
            height = Math.max(imHeight, tfLabel.height());
        }
    }

    public float reqWidth() {
        return imIcon.width() + tfLabel.width() + GAP;
    }

    public void icon(Image icon) {
        if (icon != null) {
            remove(imIcon);
            add(imIcon = icon);
        }
    }

    public void label(String label) {
        tfLabel.text(label);
    }

    public void label(String label, int color) {
        tfLabel.text(label);
        tfLabel.hardlight(color);
    }

    public void color(int color) {
        tfLabel.hardlight(color);
    }

    public float alpha() {
        return imIcon.alpha();
    }

    public void alpha(float value) {
        tfLabel.alpha(value);
        imIcon.alpha(value);
    }

    public void health(float value) {
        health.level(healthLvl = value);
        layout();
    }
}
