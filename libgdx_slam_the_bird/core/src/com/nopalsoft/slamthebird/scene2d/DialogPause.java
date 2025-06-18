package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class DialogPause extends Dialog {

    GameScreen gameScreen;

    public DialogPause(BaseScreen currentScreen) {
        super(currentScreen);
        setSize(350, 260);
        setY(300);
        setBackGround();

        gameScreen = (GameScreen) currentScreen;

        Label labelTitle = new Label("Paused", Assets.smallLabelStyle);
        labelTitle.setPosition(getWidth() / 2f - labelTitle.getWidth() / 2f, 210);

        TextButton buttonResume = new TextButton("Resume", Assets.styleTextButtonPurchased);
        screen.addPressEffect(buttonResume);
        buttonResume.setSize(150, 50);
        buttonResume.setPosition(getWidth() / 2f - buttonResume.getWidth() / 2f, 130);
        buttonResume.getLabel().setWrap(true);
        buttonResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                gameScreen.setRunningFromPaused();
            }
        });

        TextButton buttonMainMenu = new TextButton("Menu",
                Assets.styleTextButtonPurchased);
        screen.addPressEffect(buttonMainMenu);
        buttonMainMenu.setSize(150, 50);
        buttonMainMenu.setPosition(getWidth() / 2f - buttonResume.getWidth() / 2f, 40);
        buttonMainMenu.getLabel().setWrap(true);
        buttonMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                screen.changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        addActor(buttonResume);
        addActor(buttonMainMenu);
        addActor(labelTitle);
    }
}
