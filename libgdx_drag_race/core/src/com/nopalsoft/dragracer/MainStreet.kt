package com.nopalsoft.dragracer

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.dragracer.screens.BaseScreen
import com.nopalsoft.dragracer.screens.MainMenuScreen

class MainStreet : Game() {

    lateinit var stage: Stage
    lateinit var batcher: SpriteBatch


    override fun create() {

        stage =
            Stage(StretchViewport(BaseScreen.SCREEN_WIDTH.toFloat(), BaseScreen.SCREEN_HEIGHT.toFloat()))
        batcher = SpriteBatch()

        Assets.load()


        setScreen(MainMenuScreen(this))
    }
}