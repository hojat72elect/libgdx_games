package com.nopalsoft.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.superjumper.screens.MainMenuScreen;
import com.nopalsoft.superjumper.screens.Screens;

public class SuperJumperGame extends Game {


    public I18NBundle languagesBundle;
    public SpriteBatch batch;

    public Stage stage;
    public SuperJumperGame() {

    }

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        batch = new SpriteBatch();
        Settings.load();
        Assets.load();

        setScreen(new MainMenuScreen(this));
    }
}
