package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager

class PauseButton(bounds: Rectangle, private val listener: PauseButtonListener) : GameButton(bounds) {
    override fun getRegionName(): String? {
        return if (GameManager.instance.gameState == GameState.PAUSED) Constants.PLAY_REGION_NAME else Constants.PAUSE_REGION_NAME
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (GameManager.instance.gameState == GameState.OVER) {
            remove()
        }
    }

    override fun touched() {
        if (GameManager.instance.gameState == GameState.PAUSED) {
            listener.onResume()
        } else {
            listener.onPause()
        }
    }

    interface PauseButtonListener {
        fun onPause()

        fun onResume()
    }
}
