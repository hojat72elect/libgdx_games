package com.nopalsoft.dosmil.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.MainGame;
import com.nopalsoft.dosmil.Settings;
import com.nopalsoft.dosmil.game.GameScreen;

public class MainMenuScreen extends Screens {

    Image titleImage;

    Label labelPlay;
    Label labelHelp;
    Label labelLeaderboard;
    Label labelRate;

    Button buttonMusic;
    Button buttonSound;
    Button buttonFacebook;

    public MainMenuScreen(final MainGame game) {
        super(game);
        titleImage = new Image(Assets.titleAtlasRegion);
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f, 580);

        labelPlay = new Label(Assets.languagesBundle.get("play"), Assets.labelStyleLarge);
        labelPlay.setPosition(SCREEN_WIDTH / 2f - labelPlay.getWidth() / 2f, 450);
        addPressEffect(labelPlay);
        labelPlay.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        // Help
        labelHelp = new Label(Assets.languagesBundle.get("help"), Assets.labelStyleLarge);
        labelHelp.setPosition(SCREEN_WIDTH / 2f - labelHelp.getWidth() / 2f, 350);
        addPressEffect(labelHelp);
        labelHelp.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(HelpScreen.class, game);
            }
        });

        // Rate
        labelRate = new Label(Assets.languagesBundle.get("rate"), Assets.labelStyleLarge);
        labelRate.setPosition(SCREEN_WIDTH / 2f - labelRate.getWidth() / 2f, 250);
        addPressEffect(labelRate);

        // Leaderboard
        labelLeaderboard = new Label(Assets.languagesBundle.get("leaderboard"), Assets.labelStyleLarge);
        labelLeaderboard.setFontScale(.85f);
        labelLeaderboard.setWidth(SCREEN_WIDTH);
        labelLeaderboard.setPosition(SCREEN_WIDTH / 2f - labelLeaderboard.getWidth() / 2f, 150);
        labelLeaderboard.setAlignment(Align.center);
        labelLeaderboard.setWrap(true);

        addPressEffect(labelLeaderboard);

        buttonMusic = new Button(Assets.buttonStyleMusic);
        buttonMusic.setPosition(5, 5);
        buttonMusic.setChecked(!Settings.isMusicOn);
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                buttonMusic.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.playMusic();
                else
                    Assets.pauseMusic();
            }
        });

        buttonSound = new Button(Assets.buttonStyleSound);
        buttonSound.setPosition(75, 5);
        buttonSound.setChecked(!Settings.isSoundOn);
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                buttonSound.setChecked(!Settings.isSoundOn);
            }
        });

        buttonFacebook = new Button(Assets.buttonFacebook);
        buttonFacebook.setSize(50, 50);
        buttonFacebook.setPosition(SCREEN_WIDTH - buttonFacebook.getWidth() - 5, 10);
        addPressEffect(buttonFacebook);

        stage.addActor(titleImage);
        stage.addActor(labelPlay);
        stage.addActor(labelHelp);
        stage.addActor(labelLeaderboard);
        stage.addActor(labelRate);
        stage.addActor(buttonMusic);
        stage.addActor(buttonSound);
        stage.addActor(buttonFacebook);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batch.begin();
        batch.draw(Assets.backgroundAtlasRegion, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            Gdx.app.exit();
        return true;
    }
}
