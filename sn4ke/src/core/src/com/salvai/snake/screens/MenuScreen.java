package com.salvai.snake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.screens.helper.MyDialog;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.UrlController;


public class MenuScreen extends ScreenAdapter {

    public MyDialog exitDialog;
    public Button playButton;
    public TextButton websiteButton;
    private Table table;
    private final SnakeIt game;
    private final float width;
    private final float height;


    public MenuScreen(SnakeIt gameClass) {
        game = gameClass;

        width = game.worldWidth;
        height = game.worldHeight;

        game.stage.clear();
        game.setUpTopBar(Constants.SCREEN.MENU);


        setUpMainButtons();

        setUpExitDialog();
        setUpTable();
        //table.setDebug(true);

        game.stage.addActor(table);

        Gdx.input.setCatchBackKey(false);

        setUpInputMultiplexer();

        game.stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }


    public void setUpExitDialog() {
        exitDialog = new MyDialog("", game.skin, new Label("Exit?", game.skin));
        exitDialog.getContentTable().padBottom(Constants.DIALOG_BUTTON_PAD);
        final Button noButton = new Button(game.skin, "no");
        Button yesButton = new Button(game.skin, "yes");
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitDialog.hide();
                playButton.addAction(Actions.fadeIn(Constants.FADE_TIME));
                noButton.setChecked(false);
            }
        });


        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.savePreferences();
                Gdx.app.exit();
            }
        });

        exitDialog.getButtonTable().add(noButton);
        exitDialog.getButtonTable().add(yesButton);
    }

    public void setUpInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.stage);
        multiplexer.addProcessor(game.topBarStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setUpTable() {
        table = new Table(game.skin);
        table.setSize(width, height * 0.8f);
        table.setPosition(0, height * 0.1f);

        table.add(playButton).spaceBottom(height * 0.15f).size(height * 0.35f);
        table.row();
        table.add(websiteButton).height(height * 0.07f).width(width * 0.77f);
    }

    private void setUpMainButtons() {
        playButton = new Button(game.skin, "play");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new LevelChooseScreen(game));
                        dispose();
                    }
                })));
            }
        });

        websiteButton = new TextButton("simondalvai.com", game.skin);
        websiteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UrlController.openWebsite();
            }
        });
    }


    @Override
    public void render(float delta) {
        setupScreen();
        game.draw(delta);
        game.stage.act();
    }

    private void setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        game.stage.getViewport().update(width, height, true);
    }
}
