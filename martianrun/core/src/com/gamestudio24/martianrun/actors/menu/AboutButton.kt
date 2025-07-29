package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager.Companion.instance

class AboutButton(bounds: Rectangle, private val listener: AboutButtonListener) : GameButton(bounds) {
    interface AboutButtonListener {
        fun onAbout()
    }

    override fun getRegionName(): String {
        return if (instance.gameState == GameState.ABOUT) Constants.CLOSE_REGION_NAME else Constants.ABOUT_REGION_NAME
    }

    override fun touched() {
        listener.onAbout()
    }
}
