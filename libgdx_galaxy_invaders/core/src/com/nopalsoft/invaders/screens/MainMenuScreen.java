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

    TextButton btPlay, btSettings, btLeaderBoard, btMore, btFacebook;

    Label lbHighestScore;

    ImageButton btSonido, btMusica;
    Image elipseIzq;

    public MainMenuScreen(final GalaxyInvadersGame game) {
        super(game);

        Table tituloTable = new Table();
        tituloTable.setBackground(Assets.titleMenuBox);
        Label titulo = new Label(Assets.languagesBundle.get("titulo_app"), new LabelStyle(Assets.font60, Color.GREEN));
        titulo.setAlignment(Align.center);
        tituloTable.setSize(265, 100);
        tituloTable.setPosition((SCREEN_WIDTH - 265) / 2f, SCREEN_HEIGHT - 110);
        tituloTable.add(titulo).expand().center();

        // El texto se lo pongo en el update
        lbHighestScore = new Label("", new LabelStyle(Assets.font10, Color.GREEN));
        lbHighestScore.setWidth(SCREEN_WIDTH);
        lbHighestScore.setAlignment(Align.center);
        lbHighestScore.setPosition(0, SCREEN_HEIGHT - 120);

        btPlay = new TextButton(Assets.languagesBundle.get("play"), Assets.styleTextButtonMenu);
        btPlay.setSize(250, 50);
        btPlay.setPosition(0, 280);
        btPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));
            }
        });

        btSettings = new TextButton(Assets.languagesBundle.get("settings"), Assets.styleTextButtonMenu);
        btSettings.setSize(300, 50);
        btSettings.setPosition(0, 210);
        btSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new SettingsScreen(game));
            }
        });

        btLeaderBoard = new TextButton(Assets.languagesBundle.get("leaderboard"), Assets.styleTextButtonMenu);
        btLeaderBoard.setSize(310, 50);
        btLeaderBoard.setPosition(0, 140);
        btLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        btMore = new TextButton(Assets.languagesBundle.get("more"), Assets.styleTextButtonMenu);
        btMore.setSize(250, 50);
        btMore.setPosition(0, 70);
        btMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        btFacebook = new TextButton(Assets.languagesBundle.get("like_us_to_get_lastest_news"), Assets.styleTextButtonFacebook);
        btFacebook.getLabel().setWrap(true);
        btFacebook.setWidth(170);
        btFacebook.setPosition(SCREEN_WIDTH - btFacebook.getWidth() - 2, 2);
        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                Gdx.net.openURI("https://www.facebook.com/yayo28");
            }
        });

        btSonido = new ImageButton(Assets.buttonSoundOn, Assets.buttonSoundOff, Assets.buttonSoundOff);
        btSonido.setSize(40, 40);
        btSonido.setPosition(2, 2);
        if (!Settings.soundEnabled)
            btSonido.setChecked(true);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.soundEnabled = !Settings.soundEnabled;
                Assets.playSound(Assets.clickSound);
                btSonido.setChecked(!Settings.soundEnabled);
            }
        });

        btMusica = new ImageButton(Assets.buttonMusicOn, Assets.buttonMusicOff, Assets.buttonMusicOff);
        btMusica.setSize(40, 40);
        btMusica.setPosition(44, 2);
        if (!Settings.musicEnabled)
            btMusica.setChecked(true);
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.musicEnabled = !Settings.musicEnabled;
                Assets.playSound(Assets.clickSound);
                if (!Settings.musicEnabled) {
                    btMusica.setChecked(true);
                    Assets.music.pause();
                } else {
                    btMusica.setChecked(false);
                    Assets.music.play();
                }
            }
        });

        // The measurements are taken with a formula of 3 if 480 / 960 x 585 where 585 is the size,
        // 960 is the size for what they were made and 480 is the size of the camera
        elipseIzq = new Image(Assets.leftMenuEllipse);
        elipseIzq.setSize(18.5f, 292.5f);
        elipseIzq.setPosition(0, 60);

        stage.addActor(tituloTable);
        stage.addActor(lbHighestScore);

        stage.addActor(btPlay);
        stage.addActor(btSettings);
        stage.addActor(btLeaderBoard);
        stage.addActor(btMore);
        stage.addActor(elipseIzq);
        stage.addActor(btSonido);
        stage.addActor(btMusica);
        stage.addActor(btFacebook);


        if (Settings.numberOfTimesPlayed == 0) {
            game.dialog.showDialogSignIn();
        }
    }

    @Override
    public void update(float delta) {
        lbHighestScore.setText(Assets.languagesBundle.format("local_highest_score", String.valueOf(Settings.highScores[0])));
    }

    @Override
    public void draw(float delta) {
        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.disableBlending();
        Assets.backgroundLayer.render(delta);
    }

    @Override
    public boolean keyDown(int tecleada) {
        if (tecleada == Keys.BACK || tecleada == Keys.ESCAPE) {
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
