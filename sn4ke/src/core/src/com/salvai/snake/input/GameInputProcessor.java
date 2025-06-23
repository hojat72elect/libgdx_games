package com.salvai.snake.input;

import com.badlogic.gdx.InputAdapter;
import com.salvai.snake.enums.GameState;
import com.salvai.snake.screens.GameScreen;

/**
 * Created by mert on 2/1/18.
 */

public class GameInputProcessor extends InputAdapter {


    private final GameScreen gameScreen;

    public GameInputProcessor(GameScreen gameScreen) {
        super();
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (gameScreen.gameOver)
            return false;
        if (gameScreen.game.firstTimeOpen)
            gameScreen.stopTutorial();
        if (gameScreen.game.gameState == GameState.RUNNING)
            gameScreen.game.gameState = GameState.STARTED;
        return true;
    }
}
