

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;

public class CheckBox extends RedButton {

    private boolean checked = false;

    public CheckBox(String label) {
        super(label);

        icon(Icons.get(Icons.UNCHECKED));
    }

    @Override
    protected void layout() {
        super.layout();

        float margin = (height - text.height()) / 2;

        text.setPos(x + margin, y + margin);
        PixelScene.align(text);

        margin = (height - icon.height) / 2;

        icon.x = x + width - margin - icon.width;
        icon.y = y + margin;
        PixelScene.align(icon);
    }

    public boolean checked() {
        return checked;
    }

    public void checked(boolean value) {
        if (checked != value) {
            checked = value;
            icon.copy(Icons.get(checked ? Icons.CHECKED : Icons.UNCHECKED));
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        checked(!checked);
    }
}
