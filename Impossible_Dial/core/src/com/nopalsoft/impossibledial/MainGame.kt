package com.nopalsoft.impossibledial

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.nopalsoft.impossibledial.Achievements.init
import com.nopalsoft.impossibledial.Settings.load
import com.nopalsoft.impossibledial.screens.MainMenuScreen
import com.nopalsoft.impossibledial.screens.Screens

class MainGame : Game() {
    @JvmField
    var stage: Stage? = null

    @JvmField
    var batcher: SpriteBatch? = null

    override fun create() {
        stage = Stage(FitViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        batcher = SpriteBatch()

        load()
        Assets.load()
        init()
        setScreen(MainMenuScreen(this))
    }
}
