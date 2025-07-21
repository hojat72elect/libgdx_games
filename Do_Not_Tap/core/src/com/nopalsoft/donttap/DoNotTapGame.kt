package com.nopalsoft.donttap

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.donttap.handlers.FloatFormatter
import com.nopalsoft.donttap.screens.MainMenuScreen
import com.nopalsoft.donttap.screens.Screens

class DoNotTapGame(val formatter: FloatFormatter?) : Game() {

    lateinit var stage: Stage
    lateinit var batch: SpriteBatch

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        batch = SpriteBatch()
        Assets.load()
        setScreen(MainMenuScreen(this))
    }
}
