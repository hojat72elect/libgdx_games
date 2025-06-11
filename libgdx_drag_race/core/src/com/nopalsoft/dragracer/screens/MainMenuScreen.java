package com.nopalsoft.dragracer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.MainStreet;
import com.nopalsoft.dragracer.Settings;
import com.nopalsoft.dragracer.game.GameScreen;
import com.nopalsoft.dragracer.shop.ShopScreen;

public class MainMenuScreen extends Screens {

    Image titulo;

    Label lbShopScreen;
    Label lbPlay;
    Label lbLeaderboard;
    Label lbRate;

    Button btMusica;

    public MainMenuScreen(final MainStreet game) {
        super(game);

        titulo = new Image(Assets.titulo);
        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 520);
        titulo.getColor().a = 0;
        titulo.addAction(Actions.sequence(Actions.fadeIn(.5f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        stage.addActor(lbPlay);
                        stage.addActor(lbRate);
                        stage.addActor(lbLeaderboard);
                        stage.addActor(lbShopScreen);
                        stage.addActor(btMusica);
                    }
                })));

        lbPlay = new Label("Play", Assets.labelStyleGrande);
        lbPlay.setPosition(500, 440);
        lbPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        lbRate = new Label("Rate", Assets.labelStyleGrande);
        lbRate.setPosition(500, 340);

        lbShopScreen = new Label("Shop screen", Assets.labelStyleGrande);
        lbShopScreen.setPosition(500, 240);
        lbShopScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(ShopScreen.class, game);
            }
        });

        lbLeaderboard = new Label("Leaderboard", Assets.labelStyleGrande);
        lbLeaderboard.setPosition(500, 140);

        btMusica = new Button(Assets.styleButtonMusica);
        btMusica.setPosition(5, 5);
        btMusica.setChecked(!Settings.isMusicOn);
        Gdx.app.log("Musica", Settings.isMusicOn + "");
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                btMusica.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.music.play();
                else
                    Assets.music.stop();
                super.clicked(event, x, y);
            }
        });

        entranceAction(lbPlay, lbPlay.getY(), .25f);
        entranceAction(lbRate, lbRate.getY(), .5f);
        entranceAction(lbShopScreen, lbShopScreen.getY(), .75f);
        entranceAction(lbLeaderboard, lbLeaderboard.getY(), 1f);

        setAnimationChangeColor(lbShopScreen);
        setAnimationChangeColor(lbRate);
        setAnimationChangeColor(lbLeaderboard);
        setAnimationChangeColor(lbPlay);

        stage.addActor(titulo);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.calle, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT * 2);
        batcher.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            Gdx.app.exit();
        return true;

    }

}
