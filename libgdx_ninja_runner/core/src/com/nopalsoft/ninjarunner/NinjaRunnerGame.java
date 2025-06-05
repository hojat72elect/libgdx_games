package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;
import com.nopalsoft.ninjarunner.leaderboard.Person;
import com.nopalsoft.ninjarunner.screens.Screens;

public class NinjaRunnerGame extends Game {

    public Array<Person> arrayPerson = new Array<>();

    public final RequestHandler requestHandler;

    public Stage stage;
    public SpriteBatch batch;
    public I18NBundle languages;

    public NinjaRunnerGame(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void create() {
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
        batch = new SpriteBatch();
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        Settings.load();
        Assets.load();

        setScreen(new GameScreen(this, true));
    }
}
