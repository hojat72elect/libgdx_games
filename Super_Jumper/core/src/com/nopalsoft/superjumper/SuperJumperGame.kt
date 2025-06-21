package com.nopalsoft.superjumper

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.superjumper.screens.MainMenuScreen
import com.nopalsoft.superjumper.screens.Screens

class SuperJumperGame : Game() {
    @JvmField
    var languagesBundle: I18NBundle? = null

    @JvmField
    var batch: SpriteBatch? = null

    @JvmField
    var stage: Stage? = null

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))

        batch = SpriteBatch()
        Settings.load()
        Assets.load()

        setScreen(MainMenuScreen(this))
    }
}
