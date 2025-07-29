package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.thetruecolor.leaderboard.Person;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;
import com.nopalsoft.thetruecolor.screens.Screens;

public class MainTheTrueColor extends Game {
    public Array<Person> arrPerson;

    public MainTheTrueColor() {}

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {

        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();

        Settings.load();
        Assets.load();
        com.nopalsoft.thetruecolor.Achievements.init();
        setScreen(new MainMenuScreen(this));
    }
}
