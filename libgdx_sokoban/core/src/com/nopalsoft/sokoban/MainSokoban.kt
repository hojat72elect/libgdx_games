package com.nopalsoft.sokoban

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.BaseScreen

class MainSokoban : Game() {

    @JvmField
    var stage: Stage? = null

    @JvmField
    var batcher: SpriteBatch? = null

    @JvmField
    var languages: I18NBundle? = null

    override fun create() {
        stage = Stage(StretchViewport(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT))
        batcher = SpriteBatch()

        Assets.load()
        Settings.load()
        setScreen(MainMenuScreen(this))
    }
}