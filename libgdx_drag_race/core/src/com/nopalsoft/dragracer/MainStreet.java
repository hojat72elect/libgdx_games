package com.nopalsoft.dragracer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dragracer.handlers.GameServicesHandler;
import com.nopalsoft.dragracer.handlers.RequestHandler;
import com.nopalsoft.dragracer.screens.MainMenuScreen;
import com.nopalsoft.dragracer.screens.Screens;

public class MainStreet extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;

    public MainStreet(RequestHandler reqHandler, GameServicesHandler gameServiceHandler) {
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

        if (Settings.didBuyNoAds)
            reqHandler.removeAds();

        setScreen(new MainMenuScreen(this));
    }

}