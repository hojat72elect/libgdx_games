package com.nopalsoft.sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class MainSokoban extends Game {

    public Stage stage;
    public SpriteBatch batcher;
    public I18NBundle idiomas;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();

        Assets.load();
        Settings.load();
        setScreen(new MainMenuScreen(this));
    }
}
