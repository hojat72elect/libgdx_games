package com.nopalsoft.fifteen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.fifteen.screens.MainMenuScreen;
import com.nopalsoft.fifteen.screens.Screens;

public class MainFifteen extends Game {

    public Stage stage;
    public SpriteBatch batcher;

    public MainFifteen() {

    }

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH,
                Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Assets.load();

        setScreen(new MainMenuScreen(this));
    }
}