package com.salvai.centrum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.enums.GameType;
import com.salvai.centrum.utils.Constants;

public class MenuScreen extends ScreenAdapter {

    private Stage stage;
    private Table table;
    private CentrumGameClass game;
    private Label highScoreLabel;
    private float height;
    private float width;

    //buttons
    private Button playButton;
    private Button soundButton;
    private Button highscoreButton;

    public MenuScreen(CentrumGameClass gameClass) {
        game = gameClass;

        height = Gdx.graphics.getHeight() * 0.8f;
        width = Gdx.graphics.getWidth() * 0.8f;

        stage = new Stage();

        setUpMainButtons();
        setUpSettingButtons();

        highScoreLabel = new Label("BEST " + game.highScore, game.skin, "default");
        highScoreLabel.setAlignment(Align.center);

        setUpTable();
        //table.setDebug(true);

        stage.addActor(table);

        Gdx.input.setCatchBackKey(false);
        Gdx.input.setInputProcessor(stage);

        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    private void setUpTable() {
        table = new Table(game.skin);
        table.setSize(width, height);
        table.setPosition(Gdx.graphics.getWidth() * 0.5f - width * 0.5f, Gdx.graphics.getHeight() * 0.5f - height * 0.5f);
        table.defaults().expandX();

        table.add(highScoreLabel).colspan(4).height(height * 0.1f).spaceBottom(height * 0.2f).fillX();
        table.row();
        table.add(playButton).colspan(4).spaceBottom(height * 0.25f).width(height * 0.3f).height(height * 0.3f);
        table.row();
        table.add(soundButton).width(height * 0.1f).height(height * 0.1f);
        table.add(highscoreButton).width(height * 0.1f).height(height * 0.1f);
    }

    private void setUpMainButtons() {
        playButton = new Button(game.skin, "play");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.gameType = GameType.LEVEL;
                        game.setScreen(new LevelChooseScreen(game));
                        dispose();
                    }
                })));
            }
        });
    }

    private void setUpSettingButtons() {
        soundButton = new Button(game.skin, "sound");
        if (game.soundOn)
            soundButton.setChecked(false);
        else
            soundButton.setChecked(true);
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.soundOn = !game.soundOn;
                if (game.soundOn)
                    soundButton.setChecked(false);
                else
                    soundButton.setChecked(true);
                game.savePreferences();
            }
        });
        highscoreButton = new Button(game.skin, "highscore");
        // Todo save local highscore
        // highscoreButton.addListener(new ClickListener() {
        //     @Override
        //     public void clicked(InputEvent event, float x, float y) {
        //         if (game.playServices.isSessionActive())
        //             try {
        //                 game.playServices.showLeaderboards(Keyz.LEADERBOARD_ID);
        //             } catch (GameServiceException e) {
        //                 e.printStackTrace();
        //             }
        //         else
        //             game.playServices.logIn();
        //     }
        // });
    }


    @Override
    public void render(float delta) {
        setupScreen();
        game.batch.begin();
        game.drawBackground(delta);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
