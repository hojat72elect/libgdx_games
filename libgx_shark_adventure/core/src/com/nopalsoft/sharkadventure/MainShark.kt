package com.nopalsoft.sharkadventure

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.sharkadventure.game.GameScreen
import com.nopalsoft.sharkadventure.handlers.FacebookHandler
import com.nopalsoft.sharkadventure.handlers.RequestHandler
import com.nopalsoft.sharkadventure.screens.BaseScreen

class MainShark(
    val reqHandler: RequestHandler,
    val facebookHandler: FacebookHandler
) : Game() {

    lateinit var stage: Stage
    lateinit var batcher: SpriteBatch

    override fun create() {

        batcher = SpriteBatch()
        stage = Stage(StretchViewport(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT))

        Settings.load()
        Assets.load()
        Achievements.init()
        setScreen(GameScreen(this, true))

    }
}