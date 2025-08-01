package com.salvai.snake.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.input.CatchBackKeyProcessor;
import com.salvai.snake.screens.helper.PreviewSnake;
import com.salvai.snake.screens.helper.SpeedChooser;
import com.salvai.snake.utils.Constants;

public class SettingsScreen extends ScreenAdapter {

    private final int PREVIEW_BLOCKS = 6;
    //Snake colors
    private final Texture snakeTexture;
    private final Array<PreviewSnake> previewSnakes;
    private final SpeedChooser speedChooser;
    public SnakeIt game;
    float width;
    float height;
    private Table table;
    private Table snakeTable;
    private Button soundButton;
    private Button vibrationButton;


    public SettingsScreen(SnakeIt gameClass) {
        this.game = gameClass;

        width = game.worldWidth;
        height = game.worldHeight;


        previewSnakes = new Array<PreviewSnake>();
        snakeTexture = game.assetsManager.manager.get(Constants.BLOCK_IMAGE_NAME, Texture.class);

        game.stage.clear();

        game.setUpTopBar(Constants.SCREEN.SETTINGS);


        speedChooser = new SpeedChooser(game);


        setUpButtons();
        setUpTable();
        setUpSnakeTable();

//        table.setDebug(true);

        game.stage.addActor(table);


        setUpInputMultiplexer();
        game.stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(Constants.FADE_TIME)));
    }

    public void setUpInputMultiplexer() {
        Gdx.input.setCatchBackKey(true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.stage);
        multiplexer.addProcessor(game.topBarStage);
        multiplexer.addProcessor(new CatchBackKeyProcessor(game, this));
        Gdx.input.setInputProcessor(multiplexer);
    }


    private void setUpTable() {
        table = new Table(game.skin);
        table.setSize(width * 0.8f, height * 0.8f);
        table.setPosition(width * 0.1f, height * 0.1f);
        table.defaults().space(width * 0.025f).pad(40).fill();

        table.add(soundButton).size(height * 0.12f).colspan(3);
        table.add(vibrationButton).size(height * 0.12f).colspan(3);
        table.row();
        table.add(speedChooser.slider).colspan(6).height(height * 0.14f);
    }

    private void setUpSnakeTable() {
        snakeTable = new Table(game.skin);
        snakeTable.defaults().size(width * 0.07f).spaceBottom(width * 0.05f);
        snakeTable.row();
        for (int i = 0; i < Constants.COLORS_SIZE; i++) {
            final int item = i;
            //preview
            final PreviewSnake previewSnake = new PreviewSnake(item, PREVIEW_BLOCKS, snakeTexture, game.selectedColor == item);
            for (Image image : previewSnake.previews)
                snakeTable.add(image);

            for (final Image image : previewSnake.previews)
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (game.selectedColor != item) {
                            game.selectColor(item);
                            selectPreview(item);
                        }
                    }
                });

            previewSnakes.add(previewSnake);
            snakeTable.row();
        }

        table.row();
        table.add(snakeTable).colspan(6);
    }

    private void selectPreview(int index) {
        for (PreviewSnake previewSnake : previewSnakes)
            previewSnake.stopAnimation();
        previewSnakes.get(index).startAnimation();
    }

    private void setUpButtons() {
        soundButton = new Button(game.skin, "sound");
        soundButton.setChecked(!game.soundOn);
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.soundOn = !game.soundOn;
                soundButton.setChecked(!game.soundOn);
                game.savePreferences();
            }
        });


        vibrationButton = new Button(game.skin, "vibration");
        vibrationButton.setChecked(!game.vibrationOn);
        vibrationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.vibrationOn = !game.vibrationOn;
                if (game.vibrationOn) {
                    vibrationButton.setChecked(false);
                    Gdx.input.vibrate(Constants.VIBRATION_DURATION);
                } else
                    vibrationButton.setChecked(true);
                game.savePreferences();
            }
        });
    }

    @Override
    public void render(float delta) {
        setupScreen();
        game.draw(delta);
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
