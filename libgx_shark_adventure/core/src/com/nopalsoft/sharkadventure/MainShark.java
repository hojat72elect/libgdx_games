package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.sharkadventure.game.GameScreen;
import com.nopalsoft.sharkadventure.handlers.RequestHandler;
import com.nopalsoft.sharkadventure.screens.Screens;

public class MainShark extends Game {

    public final RequestHandler reqHandler;

    public Stage stage;
    public SpriteBatch batcher;

    public MainShark(RequestHandler reqHandler) {
        this.reqHandler = reqHandler;
    }

    @Override
    public void create() {

        batcher = new SpriteBatch();
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        Settings.load();
        Assets.load();
        Achievements.init();
        setScreen(new GameScreen(this, true));
    }
}
