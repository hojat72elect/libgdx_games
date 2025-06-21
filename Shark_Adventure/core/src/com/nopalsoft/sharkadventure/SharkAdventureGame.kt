package com.nopalsoft.sharkadventure

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.sharkadventure.game.GameScreen
import com.nopalsoft.sharkadventure.screens.Screens

class SharkAdventureGame : Game() {
    @JvmField
    var stage: Stage? = null

    @JvmField
    var batch: SpriteBatch? = null

    override fun create() {
        batch = SpriteBatch()
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))

        Settings.load()
        Assets.load()
        Achievements.initialize()
        setScreen(GameScreen(this, true))
    }
}
