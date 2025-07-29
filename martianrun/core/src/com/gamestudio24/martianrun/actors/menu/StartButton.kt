package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager

class StartButton(bounds: Rectangle, private val listener: StartButtonListener) : GameButton(bounds) {
    override fun getRegionName(): String? {
        return Constants.BIG_PLAY_REGION_NAME
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (GameManager.instance.gameState != GameState.OVER) {
            remove()
        }
    }

    override fun touched() {
        listener.onStart()
    }

    interface StartButtonListener {
        fun onStart()
    }
}
