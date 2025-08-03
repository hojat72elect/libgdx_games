package com.nopalsoft.ponyrace

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.ponyrace.handlers.FloatFormatter
import com.nopalsoft.ponyrace.screens.BaseScreen
import com.nopalsoft.ponyrace.screens.LoadingScreen
import com.nopalsoft.ponyrace.screens.MainMenuScreen

class PonyRacingGame(@JvmField val formatter: FloatFormatter?) : Game() {
    @JvmField
    var assetsHandler: AssetsHandler? = null
    var stage: Stage? = null
    var batch: SpriteBatch? = null

    @JvmField
    var achievementsHandler: Achievements? = null

    override fun create() {
        Settings.cargar()
        assetsHandler = AssetsHandler()
        achievementsHandler = Achievements()
        stage = Stage(StretchViewport(BaseScreen.SCREEN_WIDTH.toFloat(), BaseScreen.SCREEN_HEIGHT.toFloat()))
        batch = SpriteBatch()
        this.setScreen(LoadingScreen(this, MainMenuScreen::class.java, 1))
    }

    override fun dispose() {
        getScreen().dispose()
        stage!!.dispose()
        batch!!.dispose()
        assetsHandler!!.fontChco!!.dispose()
        assetsHandler!!.fontGde!!.dispose()
        assetsHandler!!.dispose()
        super.dispose()
    }
}
