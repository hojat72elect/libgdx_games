package com.salvai.snake.input

import com.badlogic.gdx.InputAdapter
import com.salvai.snake.enums.GameState
import com.salvai.snake.screens.GameScreen

class GameInputProcessor(private val gameScreen: GameScreen) : InputAdapter() {
    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        if (gameScreen.gameOver) return false
        if (gameScreen.game.firstTimeOpen) gameScreen.stopTutorial()
        if (gameScreen.game.gameState == GameState.RUNNING) gameScreen.game.gameState = GameState.STARTED
        return true
    }
}
