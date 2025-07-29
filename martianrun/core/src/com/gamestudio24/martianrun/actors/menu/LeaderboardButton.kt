package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager

class LeaderboardButton(bounds: Rectangle, private val listener: LeaderboardButtonListener) : GameButton(bounds) {
    interface LeaderboardButtonListener {
        fun onLeaderboard()
    }

    override fun getRegionName(): String? {
        return Constants.LEADERBOARD_REGION_NAME
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (GameManager.instance.gameState != GameState.OVER) {
            remove()
        }
    }

    override fun touched() {
        listener.onLeaderboard()
    }
}
