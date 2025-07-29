package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.math.Rectangle
import com.gamestudio24.martianrun.utils.Constants

class AchievementsButton(bounds: Rectangle, private val listener: AchievementsButtonListener) : GameButton(bounds) {
    interface AchievementsButtonListener {
        fun onAchievements()
    }

    override fun getRegionName(): String {
        return Constants.ACHIEVEMENTS_REGION_NAME
    }

    override fun touched() {
        listener.onAchievements()
    }
}
