package com.nopalsoft.ponyrace;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.ponyrace.handlers.FloatFormatter;
import com.nopalsoft.ponyrace.screens.BaseScreen;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.MainMenuScreen;

public class PonyRacingGame extends Game {

    final public FloatFormatter formatter;
    public Assets assetsHandler;
    public Stage stage;
    public SpriteBatch batch;
    public Achievements achievementsHandler;

    public PonyRacingGame(FloatFormatter formatter) {

        this.formatter = formatter;
    }

    @Override
    public void create() {
        Settings.cargar();
        assetsHandler = new Assets();
        achievementsHandler = new com.nopalsoft.ponyrace.Achievements();
        stage = new Stage(new StretchViewport(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT));
        batch = new SpriteBatch();
        this.setScreen(new LoadingScreen(this, MainMenuScreen.class, 1));
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        stage.dispose();
        batch.dispose();
        assetsHandler.fontChco.dispose();
        assetsHandler.fontGde.dispose();
        assetsHandler.dispose();
        super.dispose();
    }
}
