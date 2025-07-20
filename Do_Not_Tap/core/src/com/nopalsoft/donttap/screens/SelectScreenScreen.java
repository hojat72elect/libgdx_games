package com.nopalsoft.donttap.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.DoNotTapGame;
import com.nopalsoft.donttap.dialogs.VentanaHelp;
import com.nopalsoft.donttap.game.GameScreen;

public class SelectScreenScreen extends Screens {

    Label labelSelectGameMode;
    TextButton buttonClassic, buttonEndless, buttonTime, buttonHelp;

    Button buttonBack;

    public SelectScreenScreen(final DoNotTapGame game) {
        super(game);
        addBackGround();

        labelSelectGameMode = new Label("Select game mode", Assets.labelStyleBlack);
        labelSelectGameMode.setWidth(300);
        labelSelectGameMode.setFontScale(1.3f);
        labelSelectGameMode.setWrap(true);
        labelSelectGameMode.setPosition(
                SCREEN_WIDTH / 2f - labelSelectGameMode.getWidth() / 2f, 650);
        labelSelectGameMode.setAlignment(Align.center);

        Table menu = new Table();
        menu.setSize(200, 540);
        menu.setPosition(SCREEN_WIDTH / 2f - menu.getWidth() / 2f, 60);
        menu.defaults().center().expand();

        buttonClassic = new TextButton("Classic", Assets.textButtonStyleChico);
        addPressEffect(buttonClassic);
        buttonClassic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game,
                        GameScreen.MODE_CLASSIC);
            }
        });

        buttonTime = new TextButton("Time trial", Assets.textButtonStyleChico);
        addPressEffect(buttonTime);
        buttonTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game,
                        GameScreen.MODE_TIME);
            }
        });

        buttonEndless = new TextButton("Endless", Assets.textButtonStyleChico);
        addPressEffect(buttonEndless);
        buttonEndless.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game,
                        GameScreen.MODE_ENDLESS);
            }
        });

        buttonHelp = new TextButton("?", Assets.textButtonStyleChico);
        addPressEffect(buttonHelp);
        buttonHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new VentanaHelp(SelectScreenScreen.this).show(stage);
            }
        });

        buttonBack = new Button(new ButtonStyle(Assets.btAtras, null, null));
        buttonBack.setSize(55, 55);
        buttonBack.setPosition(5, 5);
        addPressEffect(buttonBack);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(MainMenuScreen.class, game);
                super.clicked(event, x, y);
            }
        });

        menu.add(buttonClassic);

        menu.row();
        menu.add(buttonTime);

        menu.row();
        menu.add(buttonEndless);

        menu.row();
        menu.add(buttonHelp).width(45);

        stage.addActor(labelSelectGameMode);
        stage.addActor(menu);
        stage.addActor(buttonBack);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            changeScreenWithFadeOut(MainMenuScreen.class, game);
        return true;
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void draw(float delta) {
    }

    @Override
    public void update(float delta) {
    }
}
