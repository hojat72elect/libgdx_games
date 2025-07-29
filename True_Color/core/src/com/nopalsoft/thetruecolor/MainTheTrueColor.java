package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.thetruecolor.leaderboard.Person;
import com.nopalsoft.thetruecolor.screens.BaseScreen;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;

public class MainTheTrueColor extends Game {
    public Array<Person> persons;

    public MainTheTrueColor() {}

    public Stage stage;
    public SpriteBatch batch;

    @Override
    public void create() {

        stage = new Stage(new StretchViewport(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT));
        batch = new SpriteBatch();

        Settings.load();
        Assets.load();
        Achievements.initialize();
        setScreen(new MainMenuScreen(this));
    }
}
