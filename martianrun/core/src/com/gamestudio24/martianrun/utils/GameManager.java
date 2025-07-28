package com.gamestudio24.martianrun.utils;

import com.gamestudio24.martianrun.enums.Difficulty;
import com.gamestudio24.martianrun.enums.GameState;

/**
 * A utility singleton that holds the current {@link com.gamestudio24.martianrun.enums.Difficulty}
 * and {@link com.gamestudio24.martianrun.enums.GameState} of the game.
 */
public class GameManager {
    public static final String PREFERENCES_NAME = "preferences";
    private static final GameManager ourInstance = new GameManager();

    private GameState gameState;
    private Difficulty difficulty;


    private GameManager() {
        gameState = GameState.OVER;
    }

    public static GameManager getInstance() {
        return ourInstance;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isMaxDifficulty() {
        return difficulty == Difficulty.values()[Difficulty.values().length - 1];
    }

    public void resetDifficulty() {
        setDifficulty(Difficulty.values()[0]);
    }

}
