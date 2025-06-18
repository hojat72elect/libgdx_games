package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class DialogRate extends Dialog {

    public DialogRate(BaseScreen currentScreen) {
        super(currentScreen);
        setSize(390, 260);
        setY(300);
        setBackGround();

        Label labelTitle = new Label("Support this game", Assets.smallLabelStyle);
        labelTitle.setPosition(getWidth() / 2f - labelTitle.getWidth() / 2f, 210);

        Label labelDescription = new Label(
                "Hello, thank you for playing Slam the Bird.\nHelp us to support this game. Just rate us at the app store.",
                Assets.smallLabelStyle);
        labelDescription.setSize(getWidth() - 20, 170);
        labelDescription.setPosition(getWidth() / 2f - labelDescription.getWidth() / 2f,
                50);
        labelDescription.setWrap(true);

        TextButton buttonRate = new TextButton("Rate", Assets.styleTextButtonPurchased);
        screen.addPressEffect(buttonRate);
        buttonRate.getLabel().setWrap(true);
        buttonRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        TextButton buttonNotNow = new TextButton("Not now", Assets.styleTextButtonSelected);
        screen.addPressEffect(buttonNotNow);
        buttonNotNow.getLabel().setWrap(true);
        buttonNotNow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        Table tableButtons = new Table();
        tableButtons.setSize(getWidth() - 20, 60);
        tableButtons.setPosition(getWidth() / 2f - tableButtons.getWidth() / 2f, 10);

        tableButtons.defaults().uniform().expand().center().fill().pad(10);
        tableButtons.add(buttonRate);
        tableButtons.add(buttonNotNow);

        addActor(labelDescription);
        addActor(tableButtons);
        addActor(labelTitle);
    }
}
