package com.nopalsoft.donttap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.donttap.handlers.FloatFormatter;
import com.nopalsoft.donttap.screens.MainMenuScreen;
import com.nopalsoft.donttap.screens.Screens;

public class MainDoNot extends Game {

    public final FloatFormatter formatter;

    public MainDoNot(FloatFormatter formatter) {

        this.formatter = formatter;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH,
                Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Assets.load();

        setScreen(new MainMenuScreen(this));
    }
}
