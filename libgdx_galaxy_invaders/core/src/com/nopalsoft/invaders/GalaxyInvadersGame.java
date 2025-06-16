package com.nopalsoft.invaders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.invaders.screens.MainMenuScreen;
import com.nopalsoft.invaders.screens.Screens;

public class GalaxyInvadersGame extends Game {

    public Stage stage;
    public Assets assetManager;
    public SpriteBatch batch;
    public DialogSignInGoogleGameServices dialog;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batch = new SpriteBatch();
        dialog = new DialogSignInGoogleGameServices(this, stage);

        Assets.load();
        setScreen(new MainMenuScreen(this));// Here I have to put the main screen
    }

    @Override
    public void dispose() {
        super.dispose();
        getScreen().dispose();
    }
}
