package com.salvai.snake.input;

import com.badlogic.gdx.input.GestureDetector;
import com.salvai.snake.enums.GameState;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.screens.GameScreen;

public class GameGestureDetector extends GestureDetector.GestureAdapter {

    private final GameScreen gameScreen;

    public GameGestureDetector(GameScreen gameScreen) {
        super();
        this.gameScreen = gameScreen;
    }

    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (gameScreen.gameOver)
            return false;
        MovingDirection direction = SwipeDetector.onSwipe((int) deltaX, (int) deltaY);

        if (direction != null && (gameScreen.userDirections.size == 0 || gameScreen.userDirections.peek() != direction)) {
            gameScreen.userDirections.add(direction);
            if (gameScreen.game.gameState == GameState.RUNNING)
                gameScreen.game.gameState = GameState.STARTED;
            return true;
        }
        return false;
    }
}
