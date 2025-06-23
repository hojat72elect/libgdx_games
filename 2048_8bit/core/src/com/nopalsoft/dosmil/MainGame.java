package com.nopalsoft.dosmil;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dosmil.handlers.GameServicesHandler;
import com.nopalsoft.dosmil.handlers.RequestHandler;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class MainGame extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;

    public MainGame(RequestHandler reqHandler, GameServicesHandler gameServiceHandler) {
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