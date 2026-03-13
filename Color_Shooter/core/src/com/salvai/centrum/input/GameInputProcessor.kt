package com.salvai.centrum.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.salvai.centrum.actors.player.Missile
import com.salvai.centrum.enums.GameState
import com.salvai.centrum.screens.GameScreen
import com.salvai.centrum.screens.MenuScreen
import com.salvai.centrum.utils.Constants

class GameInputProcessor(private val gameScreen: GameScreen) : InputAdapter() {
    private val touchPos: Vector3 = Vector3()

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (gameScreen.game.gameState == GameState.RUNNING) {
                gameScreen.game.gameState = GameState.PAUSED
            } else {
                gameScreen.dispose()
                gameScreen.game.setScreen(MenuScreen(gameScreen.game))
            }
        }
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchPos.set(screenX.toFloat(), screenY.toFloat(), 0f)
        gameScreen.game.camera.unproject(
            touchPos,
            gameScreen.game.viewport.screenX.toFloat(),
            gameScreen.game.viewport.screenY.toFloat(),
            gameScreen.game.viewport.screenWidth.toFloat(),
            gameScreen.game.viewport.screenHeight.toFloat()
        )
        if (gameScreen.countdownTime == 0 && gameScreen.game.gameState == GameState.RUNNING && touchPos.x != Constants.SCREEN_WIDTH * 0.5f && touchPos.y != Constants.SCREEN_HEIGHT * 0.5f) {
            gameScreen.gameFlowManager.missiles.add(Missile(Vector2(touchPos.x, touchPos.y), gameScreen.gameFlowManager.ballTexture))
        }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (gameScreen.game.gameState == GameState.PAUSED) {
            gameScreen.game.gameState = GameState.RUNNING
        }
        return true
    }
}
