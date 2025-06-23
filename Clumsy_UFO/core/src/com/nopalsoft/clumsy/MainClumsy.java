package com.nopalsoft.clumsy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.clumsy.handlers.GameServicesHandler;
import com.nopalsoft.clumsy.handlers.RequestHandler;
import com.nopalsoft.clumsy.screens.MainMenuScreen;
import com.nopalsoft.clumsy.screens.Screens;


public class MainClumsy extends Game {

    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;

    public MainClumsy(RequestHandler reqHandler, GameServicesHandler gameServiceHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
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
