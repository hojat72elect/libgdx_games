package com.nopalsoft.donttap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.DoNotTapGame;

public class MainMenuScreen extends Screens {

    TextButton buttonPlay, buttonRate, buttonLeaderboards;
    Button buttonFacebook;

    public MainMenuScreen(final DoNotTapGame game) {
        super(game);
        addBackGround();

        Image titleImage = new Image(Assets.titulo);
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f, 620);

        Table menu = new Table();
        menu.setSize(350, 400);
        menu.setPosition(SCREEN_WIDTH / 2f - menu.getWidth() / 2f, 130);
        menu.defaults().center().expand();

        buttonPlay = new TextButton("Play", Assets.textButtonStyleChico);
        addPressEffect(buttonPlay);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(SelectScreenScreen.class, game);
            }
        });

        buttonRate = new TextButton("Rate", Assets.textButtonStyleChico);
        addPressEffect(buttonRate);

        buttonLeaderboards = new TextButton("Leaderboards",
                Assets.textButtonStyleChico);
        addPressEffect(buttonLeaderboards);

        buttonFacebook = new Button(Assets.btFacebook);
        buttonFacebook.setSize(55, 55);
        buttonFacebook.setPosition(SCREEN_WIDTH - 67, 7);
        addPressEffect(buttonFacebook);

        menu.add(buttonPlay);

        menu.row();
        menu.add(buttonRate);

        menu.row();
        menu.add(buttonLeaderboards);

        stage.addActor(titleImage);
        stage.addActor(menu);
        stage.addActor(buttonFacebook);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            Gdx.app.exit();
        return true;
    }

    @Override
    public void draw(float delta) {
    }

    @Override
    public void update(float delta) {
    }
}
