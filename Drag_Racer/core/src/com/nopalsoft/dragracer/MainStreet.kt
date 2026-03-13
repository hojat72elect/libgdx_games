package com.nopalsoft.dragracer

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.dragracer.screens.MainMenuScreen
import com.nopalsoft.dragracer.screens.Screens

class MainStreet : Game() {

    var stage: Stage? = null
    var batch: SpriteBatch? = null

    override fun create() {
        stage = Stage(
            StretchViewport(
                Screens.SCREEN_WIDTH.toFloat(),
                Screens.SCREEN_HEIGHT.toFloat()
            )
        )

        batch = SpriteBatch()
        Assets.load()

        setScreen(MainMenuScreen(this))
    }
}