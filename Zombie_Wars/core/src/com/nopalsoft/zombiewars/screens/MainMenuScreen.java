package com.nopalsoft.zombiewars.screens;

import com.nopalsoft.zombiewars.MainZombieWars;
import com.nopalsoft.zombiewars.game.GameScreen;

public class MainMenuScreen extends Screens {

    boolean asd = true;

    public MainMenuScreen(MainZombieWars game) {
        super(game);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void update(float delta) {
        if (asd) {
            asd = false;
            changeScreenWithFadeOut(GameScreen.class, 0, game);
        }
    }

    @Override
    public void draw(float delta) {
        // TODO Auto-generated method stub

    }
}
