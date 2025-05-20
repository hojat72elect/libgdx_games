package com.nopalsoft.invaders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.invaders.screens.MainMenuScreen;
import com.nopalsoft.invaders.screens.Screens;

public class MainInvaders extends Game {

    public Stage stage;
    public Assets oAssets;
    public SpriteBatch spriteBatch;
    public DialogSingInGGS dialogs;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        spriteBatch = new SpriteBatch();
        dialogs = new DialogSingInGGS(this, stage);

        Assets.load();
        setScreen(new MainMenuScreen(this));// aqui tengo que poner lo principal
    }

    @Override
    public void dispose() {
        super.dispose();
        getScreen().dispose();
    }

}
