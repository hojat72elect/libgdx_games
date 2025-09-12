package com.salvador.bricks.Screens;

import static com.salvador.bricks.Objects.Constants.BUTTON_EXIT;
import static com.salvador.bricks.Objects.Constants.BUTTON_INFO;
import static com.salvador.bricks.Objects.Constants.BUTTON_PLAY;
import static com.salvador.bricks.Objects.Constants.GAME_NAME;
import static com.salvador.bricks.Objects.Constants.SCREEN_WIDTH;
import static com.salvador.bricks.Objects.ResourceManager.disposeAssets;
import static com.salvador.bricks.Objects.ResourceManager.loadAssets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.salvador.bricks.BrickBreaker;
import com.salvador.bricks.Objects.Background;
import com.salvador.bricks.Objects.MenuButton;
import com.salvador.bricks.Objects.MenuText;

public class MenuScreen extends GameClass {
    private Stage stage;
    private MenuButton playButton;
    private MenuButton exitButton;
    private MenuButton infoButton;

    public MenuScreen(BrickBreaker brickBreaker) {
        super(brickBreaker);
    }

    @Override
    public void show() {
        loadAssets();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        OrthographicCamera camera = new OrthographicCamera();
        stage.getViewport().setCamera(camera);
        camera.setToOrtho(false, 450, 800);
        Background background = new Background(0, 0);
        playButton = new MenuButton(BUTTON_PLAY, 225 - 150, 250, 300, 90);
        exitButton = new MenuButton(BUTTON_EXIT, 20, 20, 80, 80);
        infoButton = new MenuButton(BUTTON_INFO, SCREEN_WIDTH - 100, 20, 80, 80);

        MenuText titleText = new MenuText(GAME_NAME, "font.ttf", 10, 550);
        stage.addActor(background);
        stage.addActor(playButton);
        stage.addActor(exitButton);
        stage.addActor(infoButton);
        stage.addActor(titleText);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act();

        if (playButton.touch) {
            playButton.touch = false;
            game.setScreen(new GameScreen(game));
        }
        if (exitButton.touch) {
            exitButton.touch = false;
            disposeAssets();
            Gdx.app.exit();
        }
        if (infoButton.touch) {
            infoButton.touch = false;
            game.setScreen(new InfoScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
