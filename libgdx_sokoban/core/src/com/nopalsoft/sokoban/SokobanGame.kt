package com.nopalsoft.sokoban

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class SokobanGame : Game() {

    var stage: Stage? = null
    var batch: SpriteBatch? = null
    var languages: I18NBundle? = null

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        batch = SpriteBatch()

        Assets.load()
        Settings.load()

        setScreen(MainMenuScreen(this))
    }
}