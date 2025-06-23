package com.nopalsoft.impossibledial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nopalsoft.impossibledial.handlers.FacebookHandler;
import com.nopalsoft.impossibledial.handlers.GameServicesHandler;
import com.nopalsoft.impossibledial.handlers.HandlerGWT;
import com.nopalsoft.impossibledial.handlers.RequestHandler;
import com.nopalsoft.impossibledial.screens.MainMenuScreen;
import com.nopalsoft.impossibledial.screens.Screens;

public class MainGame extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;
    public final FacebookHandler facebookHandler;
    public final HandlerGWT handlerGWT;

    public MainGame(RequestHandler reqHandler, GameServicesHandler gameServiceHandler, FacebookHandler facebookHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
        this.facebookHandler = facebookHandler;
        handlerGWT = null;
    }

    public MainGame(RequestHandler reqHandler, GameServicesHandler gameServiceHandler,
                    FacebookHandler facebookHandler, HandlerGWT handlerGWT) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
        this.facebookHandler = facebookHandler;
        this.handlerGWT = handlerGWT;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {

        stage = new Stage(new FitViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();

        Settings.load();
        Assets.load();
        Achievements.init(this);
        setScreen(new MainMenuScreen(this));
//      setScreen(new GameScreen(this));
    }

}
