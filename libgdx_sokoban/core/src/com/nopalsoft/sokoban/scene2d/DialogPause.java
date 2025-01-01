package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.game.GameScreen;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

/**
 * The pause dialog that can be shown during the game.
 * It shows 3 buttons : 1- Home, 2- Refresh, 3- Toggle Animation.
 * After toggling the animation, this setting will be saved.
 */
public class DialogPause extends Dialog {

    Button buttonHome, buttonRefresh;
    Table tableAnimations;

    public DialogPause(Screens currentScreen) {
        super(currentScreen, 350, 300, 100);

        setCloseButton();
        setTitle("Paused", 1);

        Table tableMenu = new Table();
        tableMenu.setFillParent(true);

        buttonHome = new Button(Assets.btHome, Assets.btHomePress);
        buttonHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        buttonRefresh = new Button(Assets.btRefresh, Assets.btRefreshPress);
        buttonRefresh.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, screen.game);
            }
        });

        final Button btAnimations = new Button(Assets.btOff, Assets.btOn, Assets.btOn);
        btAnimations.setChecked(Settings.animationWalkIsON);

        tableAnimations = new Table();
        tableAnimations.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.animationWalkIsON = !Settings.animationWalkIsON;
                btAnimations.setChecked(Settings.animationWalkIsON);
                Settings.save();
            }
        });

        tableMenu.defaults().expandX();

        tableMenu.pad(30).padTop(55);
        tableMenu.add(buttonHome);
        tableMenu.add(buttonRefresh);
        tableMenu.row();

        Label labelAnimations = new Label("Animations", new LabelStyle(Assets.fontRed, Color.WHITE));
        tableAnimations.add(labelAnimations);
        tableAnimations.add(btAnimations).padLeft(15);

        tableMenu.add(tableAnimations).colspan(2).padTop(10);

        addActor(tableMenu);

    }

    @Override
    public void hideCompleted() {
        ((GameScreen) screen).setRunning();

    }

}
