package com.nopalsoft.thetruecolor

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.thetruecolor.Achievements.initialize
import com.nopalsoft.thetruecolor.leaderboard.Person
import com.nopalsoft.thetruecolor.screens.BaseScreen
import com.nopalsoft.thetruecolor.screens.MainMenuScreen

class TrueColorGame : Game() {
    var persons: Array<Person?>? = null
    var stage: Stage? = null
    var batch: SpriteBatch? = null

    override fun create() {
        stage = Stage(StretchViewport(BaseScreen.SCREEN_WIDTH.toFloat(), BaseScreen.SCREEN_HEIGHT.toFloat()))
        batch = SpriteBatch()

        Settings.load()
        Assets.load()
        initialize()
        setScreen(MainMenuScreen(this))
    }
}
