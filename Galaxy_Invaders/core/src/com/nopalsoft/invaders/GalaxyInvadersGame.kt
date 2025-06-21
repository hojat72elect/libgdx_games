package com.nopalsoft.invaders

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.invaders.screens.MainMenuScreen
import com.nopalsoft.invaders.screens.Screens

class GalaxyInvadersGame : Game() {
    @JvmField
    var stage: Stage? = null

    @JvmField
    var assetManager: Assets? = null

    @JvmField
    var batch: SpriteBatch? = null

    @JvmField
    var dialog: DialogSignInGoogleGameServices? = null

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        batch = SpriteBatch()
        dialog = DialogSignInGoogleGameServices(this, stage!!)

        Assets.load()
        setScreen(MainMenuScreen(this)) // Here I have to put the main screen
    }

    override fun dispose() {
        super.dispose()
        getScreen().dispose()
    }
}
