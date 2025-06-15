package com.nopalsoft.invaders.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.GalaxyInvadersGame;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.game.GameScreen;

public class MainMenuScreen extends Screens {

    TextButton buttonPlay, buttonSettings, buttonLeaderBoard, buttonMore, buttonFacebook;

    Label labelHighestScore;

    ImageButton buttonSound, buttonMusic;
    Image leftEllipse;

    public MainMenuScreen(final GalaxyInvadersGame game) {
        super(game);

        Table tableTitle = new Table();
        tableTitle.setBackground(Assets.titleMenuBox);
        Label labelTitle = new Label(Assets.languagesBundle.get("titulo_app"), new LabelStyle(Assets.font60, Color.GREEN));
        labelTitle.setAlignment(Align.center);
        tableTitle.setSize(265, 100);
        tableTitle.setPosition((SCREEN_WIDTH - 265) / 2f, SCREEN_HEIGHT - 110);
        tableTitle.add(labelTitle).expand().center();

        // I'll put the text in the update.
        labelHighestScore = new Label("", new LabelStyle(Assets.font10, Color.GREEN));
        labelHighestScore.setWidth(SCREEN_WIDTH);
        labelHighestScore.setAlignment(Align.center);
        labelHighestScore.setPosition(0, SCREEN_HEIGHT - 120);

        buttonPlay = new TextButton(Assets.languagesBundle.get("play"), Assets.styleTextButtonMenu);
        buttonPlay.setSize(250, 50);
        buttonPlay.setPosition(0, 280);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));
            }
        });

        buttonSettings = new TextButton(Assets.languagesBundle.get("settings"), Assets.styleTextButtonMenu);
        buttonSettings.setSize(300, 50);
        buttonSettings.setPosition(0, 210);
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new SettingsScreen(game));
            }
        });

        buttonLeaderBoard = new TextButton(Assets.languagesBundle.get("leaderboard"), Assets.styleTextButtonMenu);
        buttonLeaderBoard.setSize(310, 50);
        buttonLeaderBoard.setPosition(0, 140);
        buttonLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        buttonMore = new TextButton(Assets.languagesBundle.get("more"), Assets.styleTextButtonMenu);
        buttonMore.setSize(250, 50);
        buttonMore.setPosition(0, 70);
        buttonMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        buttonFacebook = new TextButton(Assets.languagesBundle.get("like_us_to_get_lastest_news"), Assets.styleTextButtonFacebook);
        buttonFacebook.getLabel().setWrap(true);
        buttonFacebook.setWidth(170);
        buttonFacebook.setPosition(SCREEN_WIDTH - buttonFacebook.getWidth() - 2, 2);
        buttonFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                Gdx.net.openURI("https://www.facebook.com/yayo28");
            }
        });

        buttonSound = new ImageButton(Assets.buttonSoundOn, Assets.buttonSoundOff, Assets.buttonSoundOff);
        buttonSound.setSize(40, 40);
        buttonSound.setPosition(2, 2);
        if (!Settings.soundEnabled)
            buttonSound.setChecked(true);
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.soundEnabled = !Settings.soundEnabled;
                Assets.playSound(Assets.clickSound);
                buttonSound.setChecked(!Settings.soundEnabled);
            }
        });

        buttonMusic = new ImageButton(Assets.buttonMusicOn, Assets.buttonMusicOff, Assets.buttonMusicOff);
        buttonMusic.setSize(40, 40);
        buttonMusic.setPosition(44, 2);
        if (!Settings.musicEnabled)
            buttonMusic.setChecked(true);
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.musicEnabled = !Settings.musicEnabled;
                Assets.playSound(Assets.clickSound);
                if (!Settings.musicEnabled) {
                    buttonMusic.setChecked(true);
                    Assets.music.pause();
                } else {
                    buttonMusic.setChecked(false);
                    Assets.music.play();
                }
            }
        });

        // The measurements are taken with a formula of 3 if 480 / 960 x 585 where 585 is the size,
        // 960 is the size for what they were made and 480 is the size of the camera
        leftEllipse = new Image(Assets.leftMenuEllipse);
        leftEllipse.setSize(18.5f, 292.5f);
        leftEllipse.setPosition(0, 60);

        stage.addActor(tableTitle);
        stage.addActor(labelHighestScore);

        stage.addActor(buttonPlay);
        stage.addActor(buttonSettings);
        stage.addActor(buttonLeaderBoard);
        stage.addActor(buttonMore);
        stage.addActor(leftEllipse);
        stage.addActor(buttonSound);
        stage.addActor(buttonMusic);
        stage.addActor(buttonFacebook);


        if (Settings.numberOfTimesPlayed == 0) {
            game.dialog.showDialogSignIn();
        }
    }

    @Override
    public void update(float delta) {
        labelHighestScore.setText(Assets.languagesBundle.format("local_highest_score", String.valueOf(Settings.highScores[0])));
    }

    @Override
    public void draw(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.disableBlending();
        Assets.backgroundLayer.render(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            if (game.dialog.isDialogShown()) {
                game.dialog.dismissAll();
            } else {

                Gdx.app.exit();
            }
            return true;
        }
        return false;
    }
}
