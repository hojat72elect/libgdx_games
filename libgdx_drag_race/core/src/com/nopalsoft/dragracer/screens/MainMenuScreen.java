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

    Image titleImage;

    Label labelShopScreen;
    Label labelPlay;
    Label labelLeaderBoard;
    Label labelRate;

    Button buttonMusic;

    public MainMenuScreen(final MainStreet game) {
        super(game);

        titleImage = new Image(Assets.titulo);
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f, 520);
        titleImage.getColor().a = 0;
        titleImage.addAction(Actions.sequence(Actions.fadeIn(.5f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        stage.addActor(labelPlay);
                        stage.addActor(labelRate);
                        stage.addActor(labelLeaderBoard);
                        stage.addActor(labelShopScreen);
                        stage.addActor(buttonMusic);
                    }
                })));

        labelPlay = new Label("Play", Assets.labelStyleGrande);
        labelPlay.setPosition(500, 440);
        labelPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        labelRate = new Label("Rate", Assets.labelStyleGrande);
        labelRate.setPosition(500, 340);

        labelShopScreen = new Label("Shop screen", Assets.labelStyleGrande);
        labelShopScreen.setPosition(500, 240);
        labelShopScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(ShopScreen.class, game);
            }
        });

        labelLeaderBoard = new Label("Leaderboard", Assets.labelStyleGrande);
        labelLeaderBoard.setPosition(500, 140);

        buttonMusic = new Button(Assets.styleButtonMusica);
        buttonMusic.setPosition(5, 5);
        buttonMusic.setChecked(!Settings.isMusicOn);
        Gdx.app.log("Musica", Settings.isMusicOn + "");
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                buttonMusic.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.music.play();
                else
                    Assets.music.stop();
                super.clicked(event, x, y);
            }
        });

        entranceAction(labelPlay, labelPlay.getY(), .25f);
        entranceAction(labelRate, labelRate.getY(), .5f);
        entranceAction(labelShopScreen, labelShopScreen.getY(), .75f);
        entranceAction(labelLeaderBoard, labelLeaderBoard.getY(), 1f);

        setAnimationChangeColor(labelShopScreen);
        setAnimationChangeColor(labelRate);
        setAnimationChangeColor(labelLeaderBoard);
        setAnimationChangeColor(labelPlay);

        stage.addActor(titleImage);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batch.begin();
        batch.draw(Assets.calle, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT * 2);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            Gdx.app.exit();
        return true;
    }
}
