package com.salvador.bricks.screens;

import com.badlogic.gdx.Screen;
import com.salvador.bricks.BrickBreaker;

public abstract class GameClass implements Screen {

    public BrickBreaker game;

    public GameClass(BrickBreaker game) {
        this.game = game;
    }
}
