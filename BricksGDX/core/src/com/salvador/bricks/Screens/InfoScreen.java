package com.salvador.bricks.Screens;

import static com.salvador.bricks.Objects.Constants.BUTTON_EXIT;
import static com.salvador.bricks.Objects.Constants.GAME_NAME;
import static com.salvador.bricks.Objects.ResourceManager.loadAssets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.salvador.bricks.BrickBreaker;
import com.salvador.bricks.Objects.Background;
import com.salvador.bricks.Objects.MenuButton;
import com.salvador.bricks.Objects.MenuText;

import java.util.ArrayList;

public class InfoScreen extends GameClass {

    private Stage stage;
    private MenuButton exitButton;
    private final ArrayList<MenuText> credits = new ArrayList<MenuText>();
    public InfoScreen(BrickBreaker brickBreaker) {
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
        exitButton = new MenuButton(BUTTON_EXIT, 20, 20, 80, 80);

        credits.add(newTitle(GAME_NAME, 650));
        credits.add(newTitle("Developer", 550));
        credits.add(newTitle("Salvador Valverde", 500));
        credits.add(newTitle("Graphics", 400));
        credits.add(newTitle("Kenney", 350));
        credits.add(newTitle("Audio", 250));
        credits.add(newTitle("Nadie", 200));

        stage.addActor(background);
        stage.addActor(exitButton);
        for (MenuText menuText : credits) {
            stage.addActor(menuText);
        }
    }

    public MenuText newTitle(String title, float y) {
        return (new MenuText(title, "font20.ttf", 10, y));
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act();

        if (exitButton.touch) {
            game.setScreen(new MenuScreen(game));
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
