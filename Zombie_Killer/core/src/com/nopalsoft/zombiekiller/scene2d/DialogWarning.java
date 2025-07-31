package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.screens.Screens;

public class DialogWarning extends Dialog {

    public DialogWarning(Screens currentScreen, String text) {
        super(currentScreen, 350, 200, 150, Assets.backgroundSmallWindow);
        setCloseButton(305, 155, 45);

        Label labelShop = new Label(text, Assets.labelStyleChico);
        labelShop.setFontScale(1);
        labelShop.setWrap(true);
        labelShop.setAlignment(Align.center);
        labelShop.setWidth(getWidth() - 20);

        labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 90);
        addActor(labelShop);
    }
}
