package com.nopalsoft.donttap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.MainDoNot;

public class MainMenuScreen extends Screens {

    Label lbLeaderboard;

    TextButton btPlay, btRate, btLeaderboards;
    Button btFacebook;

    public MainMenuScreen(final MainDoNot game) {
        super(game);
        addBackGround();

        Image titulo = new Image(Assets.titulo);
        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 620);

        Table menu = new Table();
        menu.setSize(350, 400);
        // menu.debug();
        menu.setPosition(SCREEN_WIDTH / 2f - menu.getWidth() / 2f, 130);
        menu.defaults().center().expand();

        btPlay = new TextButton("Play", Assets.textButtonStyleChico);
        addEfectoPress(btPlay);
        btPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(SelectScreenScreen.class, game);

            }
        });

        btRate = new TextButton("Rate", Assets.textButtonStyleChico);
        addEfectoPress(btRate);
        btRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showRater();

            }
        });

        btLeaderboards = new TextButton("Leaderboards",
                Assets.textButtonStyleChico);
        addEfectoPress(btLeaderboards);
        btLeaderboards.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getLeaderboard();
                } else {
                    game.gameServiceHandler.signIn();
                }

            }
        });

        btFacebook = new Button(Assets.btFacebook);
        btFacebook.setSize(55, 55);
        btFacebook.setPosition(SCREEN_WIDTH - 67, 7);
        addEfectoPress(btFacebook);
        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showFacebook();
            }
        });

        menu.add(btPlay);

        menu.row();
        menu.add(btRate);

        menu.row();
        menu.add(btLeaderboards);

        stage.addActor(titulo);
        stage.addActor(menu);
        stage.addActor(btFacebook);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            Gdx.app.exit();
        return true;

    }

    @Override
    public void draw(float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(float delta) {

    }

}
