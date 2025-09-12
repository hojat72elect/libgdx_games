package com.salvador.bricks;

import com.badlogic.gdx.Game;
import com.salvador.bricks.Screens.MenuScreen;

public class BrickBreaker extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
