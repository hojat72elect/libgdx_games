package com.nopalsoft.zombiekiller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.zombiekiller.handlers.FacebookHandler;
import com.nopalsoft.zombiekiller.handlers.GameServicesHandler;
import com.nopalsoft.zombiekiller.handlers.RequestHandler;
import com.nopalsoft.zombiekiller.screens.MainMenuScreen;
import com.nopalsoft.zombiekiller.screens.Screens;

public class MainZombie extends Game {
    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;
    public final FacebookHandler facebookHandler;

    public I18NBundle idiomas;

    public MainZombie(RequestHandler reqHandler, GameServicesHandler gameServiceHandler, FacebookHandler facebookHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
        this.facebookHandler = facebookHandler;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {
        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Assets.load();
        Achievements.init(this);

        setScreen(new MainMenuScreen(this));
    }

}
