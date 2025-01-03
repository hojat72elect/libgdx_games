package com.nopalsoft.zombiewars.screens

import com.nopalsoft.zombiewars.MainZombieWars
import com.nopalsoft.zombiewars.game.GameScreen

class MainMenuScreen(game: MainZombieWars) : Screens(game) {
    var asd: Boolean = true

    override fun update(delta: Float) {
        if (asd) {
            asd = false
            changeScreenWithFadeOut(GameScreen::class.java, game)
        }
    }

    override fun draw(delta: Float) {
    }
}
