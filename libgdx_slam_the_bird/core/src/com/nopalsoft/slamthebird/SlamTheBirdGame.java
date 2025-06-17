package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class SlamTheBirdGame extends Game {

    public SlamTheBirdGame() {

    }

    public Stage stage;
    public SpriteBatch batch;

    @Override
    public void create() {
        stage = new Stage(new StretchViewport(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT));

        batch = new SpriteBatch();
        Assets.load();
        Achievements.init();

        setScreen(new GameScreen(this));
    }
}