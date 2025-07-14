package com.nopalsoft.dosmil

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.dosmil.screens.MainMenuScreen
import com.nopalsoft.dosmil.screens.Screens

class MainGame : Game() {

    var stage: Stage? = null

    var batch: SpriteBatch? = null

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        batch = SpriteBatch()
        Assets.load()
        setScreen(MainMenuScreen(this))
    }
}