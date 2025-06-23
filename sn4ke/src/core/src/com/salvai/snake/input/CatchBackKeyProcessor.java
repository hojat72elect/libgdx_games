package com.salvai.snake.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.salvai.snake.SnakeIt;
import com.salvai.snake.screens.LevelChooseScreen;
import com.salvai.snake.screens.MenuScreen;
import com.salvai.snake.screens.SettingsScreen;
import com.salvai.snake.utils.Constants;

public class CatchBackKeyProcessor extends InputAdapter {
    private SnakeIt game;
    private Screen screen;

    public CatchBackKeyProcessor(SnakeIt game, Screen screen) {
        this.game = game;
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if (screen instanceof LevelChooseScreen) {
                ((LevelChooseScreen) screen).game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game));
                        screen.dispose();
                    }
                })));
            } else if (screen instanceof SettingsScreen) {
                ((SettingsScreen) screen).game.stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game));
                        screen.dispose();
                    }
                })));
            } else return !(screen instanceof MenuScreen);
        }
        return true;
    }
}
