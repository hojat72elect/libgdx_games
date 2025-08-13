package com.nopalsoft.zombiewars

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.zombiewars.screens.MainMenuScreen
import com.nopalsoft.zombiewars.screens.Screens

class MainZombieWars : Game() {

    lateinit var stage: Stage
    lateinit var batcher: SpriteBatch

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        batcher = SpriteBatch()

        Assets.load()
        setScreen(MainMenuScreen(this))
    }
}
