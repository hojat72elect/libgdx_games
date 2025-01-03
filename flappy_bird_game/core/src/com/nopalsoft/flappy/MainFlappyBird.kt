package com.nopalsoft.flappy

import com.badlogic.gdx.Game
import com.nopalsoft.flappy.game.GameScreen

class MainFlappyBird : Game() {
    override fun create() {
        // Load all the game assets
        Assets.load()
        // Go to the GameScreen
        setScreen(GameScreen(this))
    }
}
