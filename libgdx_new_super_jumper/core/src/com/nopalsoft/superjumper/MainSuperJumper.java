package com.nopalsoft.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.superjumper.handlers.GameServicesHandler;
import com.nopalsoft.superjumper.handlers.RequestHandler;
import com.nopalsoft.superjumper.screens.MainMenuScreen;
import com.nopalsoft.superjumper.screens.Screens;

public class MainSuperJumper extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;

    public I18NBundle idiomas;

    public MainSuperJumper(RequestHandler reqHandler, GameServicesHandler gameServiceHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Settings.load();
        Assets.load();

        setScreen(new MainMenuScreen(this));
    }
}
