package com.salvador.bricks

import com.badlogic.gdx.Game
import com.salvador.bricks.screens.MenuScreen

class BrickBreaker : Game() {
    override fun create() {
        setScreen(MenuScreen(this))
    }
}
