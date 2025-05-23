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

public class WindowGroupPause extends WindowGroup {

    Button buttonHome, buttonRefresh;
    Table tableAnimations;

    public WindowGroupPause(Screens currentScreen) {
        super(currentScreen, 350, 300, 100);

        setCloseButton();
        setTitle("Paused", 1);

        Table tableMenu = new Table();
        tableMenu.setFillParent(true);

        buttonHome = new Button(Assets.homeButtonDrawable, Assets.homeButtonPressedDrawable);
        buttonHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        buttonRefresh = new Button(Assets.buttonRefreshDrawable, Assets.buttonRefreshPressedDrawable);
        buttonRefresh.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, screen.game);
            }
        });

        final Button buttonAnimation = new Button(Assets.buttonOffDrawable, Assets.buttonOnDrawable, Assets.buttonOnDrawable);
        buttonAnimation.setChecked(Settings.animationWalkIsON);

        tableAnimations = new Table();
        tableAnimations.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.animationWalkIsON = !Settings.animationWalkIsON;
                buttonAnimation.setChecked(Settings.animationWalkIsON);
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
        tableAnimations.add(buttonAnimation).padLeft(15);

        tableMenu.add(tableAnimations).colspan(2).padTop(10);

        addActor(tableMenu);
    }

}
