package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.zombiekiller.Assets;

public class LabelDialog extends Table {

    Label label;

    public LabelDialog(String text, boolean isInverted) {

        label = new Label(text, Assets.labelStyleHelpDialog);
        label.setWrap(true);

        float width = 75;
        float height = 100;

        if (label.getWidth() > width)
            width = label.getWidth();
        if (label.getHeight() > height)
            height = label.getHeight();

        setSize(width, height);

        if (isInverted) {
            defaults().pad(45, 10, 10, 10);
            pad(0);
            setBackground(Assets.helpDialogInverted);
        } else {
            defaults().pad(10, 10, 45, 10);
            pad(0);
            setBackground(Assets.helpDialog);
        }

        add(label).expand().fill();
    }

    @Override
    public void setPosition(float x, float y) {
        x -= 35;
        super.setPosition(x, y);
    }
}
