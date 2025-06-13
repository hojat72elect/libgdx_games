package com.nopalsoft.dragracer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dragracer.screens.MainMenuScreen;
import com.nopalsoft.dragracer.screens.Screens;

public class MainStreet extends Game {

    public MainStreet() {

    }

    public Stage stage;
    public SpriteBatch batch;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH,
                Screens.SCREEN_HEIGHT));

        batch = new SpriteBatch();
        Assets.load();

        setScreen(new MainMenuScreen(this));
    }
}