package com.nopalsoft.zombiedash;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.zombiedash.handlers.FacebookHandler;
import com.nopalsoft.zombiedash.handlers.GameServicesHandler;
import com.nopalsoft.zombiedash.handlers.RequestHandler;
import com.nopalsoft.zombiedash.screens.MainMenuScreen;
import com.nopalsoft.zombiedash.screens.Screens;

public class MainZombieDash extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;
    public final FacebookHandler facebookHandler;

    public I18NBundle idiomas;

    public MainZombieDash(RequestHandler reqHandler, GameServicesHandler gameServiceHandler, FacebookHandler facebookHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
        this.facebookHandler = facebookHandler;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Assets.load();
        Achievements.init(this);

        setScreen(new MainMenuScreen(this));
    }
}
