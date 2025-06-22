package com.nopalsoft.slamthebird

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.screens.BaseScreen

class SlamTheBirdGame : Game() {

    var stage: Stage? = null
    var batch: SpriteBatch? = null

    override fun create() {
        stage = Stage(StretchViewport(BaseScreen.SCREEN_WIDTH.toFloat(), BaseScreen.SCREEN_HEIGHT.toFloat()))

        batch = SpriteBatch()
        Assets.load()
        Achievements.init()

        setScreen(GameScreen(this))
    }
}