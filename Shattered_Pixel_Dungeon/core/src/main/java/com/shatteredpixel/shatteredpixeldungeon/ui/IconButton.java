

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class IconButton extends Button {

    protected Image icon;

    public IconButton() {
        super();
    }

    public IconButton(Image icon) {
        super();
        icon(icon);
    }

    @Override
    protected void layout() {
        super.layout();

        if (icon != null) {
            icon.x = x + (width - icon.width()) / 2f;
            icon.y = y + (height - icon.height()) / 2f;
            PixelScene.align(icon);
        }
    }

    @Override
    protected void onPointerDown() {
        if (icon != null) icon.brightness(1.5f);
        Sample.INSTANCE.play(Assets.Sounds.CLICK);
    }

    @Override
    protected void onPointerUp() {
        if (icon != null) icon.resetColor();
    }

    public void enable(boolean value) {
        active = value;
        if (icon != null) icon.alpha(value ? 1.0f : 0.3f);
    }

    public void icon(Image icon) {
        if (this.icon != null) {
            remove(this.icon);
        }
        this.icon = icon;
        if (this.icon != null) {
            add(this.icon);
            layout();
        }
    }

    public Image icon() {
        return icon;
    }
}
