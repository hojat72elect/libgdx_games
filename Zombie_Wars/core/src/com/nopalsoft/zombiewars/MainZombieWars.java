package com.nopalsoft.zombiewars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.zombiewars.screens.MainMenuScreen;
import com.nopalsoft.zombiewars.screens.Screens;

public class MainZombieWars extends Game {


    public I18NBundle idiomas;
    public Stage stage;
    public SpriteBatch batcher;
    public MainZombieWars() {
    }

    @Override
    public void create() {

        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Assets.load();


        setScreen(new MainMenuScreen(this));
    }
}
