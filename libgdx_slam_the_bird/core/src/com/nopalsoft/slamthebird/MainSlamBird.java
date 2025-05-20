package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.handlers.GameServicesHandler;
import com.nopalsoft.slamthebird.handlers.RequestHandler;
import com.nopalsoft.slamthebird.screens.Screens;

public class MainSlamBird extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;

    public MainSlamBird(RequestHandler reqHandler, GameServicesHandler gameServiceHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Assets.load();
        Achievements.init(this);

        if (Settings.didBuyNoAds)
            reqHandler.removeAds();

        setScreen(new GameScreen(this));
    }

}