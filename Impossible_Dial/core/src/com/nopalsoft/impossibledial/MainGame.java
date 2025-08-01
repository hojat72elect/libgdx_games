package com.nopalsoft.impossibledial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nopalsoft.impossibledial.screens.MainMenuScreen;
import com.nopalsoft.impossibledial.screens.Screens;

public class MainGame extends Game {

    public Stage stage;
    public SpriteBatch batcher;

    public MainGame() {

    }

    @Override
    public void create() {

        stage = new Stage(new FitViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();

        Settings.load();
        Assets.load();
        Achievements.init();
        setScreen(new MainMenuScreen(this));
    }
}
