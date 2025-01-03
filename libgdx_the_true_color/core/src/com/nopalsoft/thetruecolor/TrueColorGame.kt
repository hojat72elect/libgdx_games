package com.nopalsoft.thetruecolor

import com.badlogic.gdx.utils.Array as GdxArray
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.thetruecolor.leaderboard.Person
import com.nopalsoft.thetruecolor.screens.BaseScreen
import com.nopalsoft.thetruecolor.screens.MainMenuScreen

class TrueColorGame : Game() {

    @JvmField
    var arrPerson: GdxArray<Person>? = null
    lateinit var stage: Stage
    lateinit var batcher: SpriteBatch

    override fun create() {
        stage =
            Stage(StretchViewport(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT))
        batcher = SpriteBatch()

        Settings.load()
        Assets.load()

        screen = MainMenuScreen(this)
    }

}