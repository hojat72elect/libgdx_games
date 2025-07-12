package com.nopalsoft.dosmil;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class MainGame extends Game {

    public MainGame() {
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