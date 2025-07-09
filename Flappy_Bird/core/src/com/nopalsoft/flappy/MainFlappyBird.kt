package com.nopalsoft.flappy

import com.badlogic.gdx.Game
import com.nopalsoft.flappy.game.GameScreen

class MainFlappyBird : Game() {
    override fun create() {
        Assets.load()
        setScreen(GameScreen(this))
    }
}
