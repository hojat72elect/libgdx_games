package com.nopalsoft.lander

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.nopalsoft.lander.screens.MainMenuScreen

class MainLander : Game() {

    lateinit var stage: Stage
    lateinit var batcher: SpriteBatch

    override fun create() {
        Assets.cargar()
        stage = Stage()
        batcher = SpriteBatch()

        setScreen(MainMenuScreen(this))
    }
}