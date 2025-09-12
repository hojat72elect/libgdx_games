package com.salvador.bricks.Screens;

import static com.salvador.bricks.Objects.Constants.BUTTON_RESET;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.salvador.bricks.BrickBreaker;
import com.salvador.bricks.Objects.Background;
import com.salvador.bricks.Objects.MenuButton;
import com.salvador.bricks.Objects.MenuText;

public class GameOverScreen extends GameClass {
    private final float score;
    public float scoreI = 1;
    long start = 0;
    private Stage stage;
    private MenuButton menuButton;
    private MenuText scoreText;

    public GameOverScreen(BrickBreaker brickBreaker, float score) {
        super(brickBreaker);
        this.score = score;
    }

    @Override
    public void show() {
        stage = new Stage();
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);
        OrthographicCamera camera = new OrthographicCamera();
        stage.getViewport().setCamera(camera);
        camera.setToOrtho(false, 450, 800);
        Background background = new Background(0, 0);
        menuButton = new MenuButton(BUTTON_RESET, 225 - 150, 250, 300, 90);
        MenuText titleText = new MenuText("Game Over", "font.ttf", 10, 600);
        scoreText = new MenuText("Score: " + "0", "font20.ttf", 300, 500);
        stage.addActor(background);
        stage.addActor(menuButton);
        stage.addActor(titleText);
        stage.addActor(scoreText);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act();

        if (scoreI < score) {
            long diffInMillis = TimeUtils.timeSinceMillis(start);
            if (diffInMillis > 10) {
                scoreI = scoreI + 5;
                scoreText.setText(String.valueOf(scoreI));
                start = 0;
            }
        }

        if (menuButton.touch) {
            menuButton.touch = false;
            game.setScreen(new GameScreen(game));
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
