package com.nopalsoft.zombiekiller

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.zombiekiller.screens.MainMenuScreen
import com.nopalsoft.zombiekiller.screens.Screens

class MainZombie : Game() {
    var idiomas: I18NBundle? = null
    var stage: Stage? = null
    var batcher: SpriteBatch? = null
    override fun create() {
        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))

        batcher = SpriteBatch()
        Assets.load()
        Achievements.init()

        setScreen(MainMenuScreen(this))
    }
}
