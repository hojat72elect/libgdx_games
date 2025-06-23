package com.salvai.centrum.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.salvai.centrum.CentrumGameClass;
import com.salvai.centrum.enums.GameType;
import com.salvai.centrum.screens.GameOverScreen;
import com.salvai.centrum.screens.LevelChooseScreen;
import com.salvai.centrum.screens.MenuScreen;
import com.salvai.centrum.utils.Constants;

public class CatchBackKeyProcessor extends InputAdapter {
    private CentrumGameClass game;
    private Screen screen;

    public CatchBackKeyProcessor(CentrumGameClass game, Screen screen) {
        this.game = game;
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if (screen instanceof LevelChooseScreen) {
                ((LevelChooseScreen) screen).stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                            game.setScreen(new MenuScreen(game));
                        screen.dispose();
                    }
                })));
            } else if (screen instanceof MenuScreen)
                return false;
            else if (screen instanceof GameOverScreen) {

                ((GameOverScreen) screen).stage.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (game.gameType == GameType.LEVEL)
                            game.setScreen(new LevelChooseScreen(game));
                        else
                            game.setScreen(new MenuScreen(game));
                        screen.dispose();
                    }
                })));

            }

        }
        return true;
    }
}
