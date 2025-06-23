package com.nopalsoft.ponyrace;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.ponyrace.handlers.FloatFormatter;
import com.nopalsoft.ponyrace.handlers.GameServicesHandler;
import com.nopalsoft.ponyrace.handlers.RequestHandler;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.MainMenuScreen;
import com.nopalsoft.ponyrace.screens.Screens;

public class MainPonyRace extends Game {
    final public RequestHandler reqHandler;
    final public GameServicesHandler gameServiceHandler;
    final public FloatFormatter formatter;

    public MainPonyRace(RequestHandler handler, GameServicesHandler gameServiceHandler, FloatFormatter formatter) {
        this.gameServiceHandler = gameServiceHandler;
        this.reqHandler = handler;
        this.formatter = formatter;
    }

    public Assets oAssets;
    public Stage stage;
    public SpriteBatch batcher;
    public com.nopalsoft.ponyrace.Achievements achievements;

    @Override
    public void create() {
        Settings.cargar();
        oAssets = new Assets();
        achievements = new com.nopalsoft.ponyrace.Achievements(this);
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();
        this.setScreen(new LoadingScreen(this, MainMenuScreen.class, 1));

    }

    String status;

    @Override
    public void dispose() {
        getScreen().dispose();
        stage.dispose();
        batcher.dispose();
        oAssets.fontChco.dispose();
        oAssets.fontGde.dispose();
        oAssets.dispose();
        super.dispose();
    }

}
