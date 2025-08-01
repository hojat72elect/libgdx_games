package com.salvai.snake.input

import com.badlogic.gdx.input.GestureDetector.GestureAdapter
import com.salvai.snake.enums.GameState
import com.salvai.snake.input.SwipeDetector.onSwipe
import com.salvai.snake.screens.GameScreen

class GameGestureDetector(private val gameScreen: GameScreen) : GestureAdapter() {
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        if (gameScreen.gameOver) return false
        val direction = onSwipe(deltaX.toInt(), deltaY.toInt())

        if (direction != null && (gameScreen.userDirections.size == 0 || gameScreen.userDirections.peek() != direction)) {
            gameScreen.userDirections.add(direction)
            if (gameScreen.game.gameState == GameState.RUNNING) gameScreen.game.gameState = GameState.STARTED
            return true
        }
        return false
    }
}
