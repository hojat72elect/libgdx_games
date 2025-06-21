package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.leaderboard.Person
import com.nopalsoft.ninjarunner.screens.Screens
import com.badlogic.gdx.utils.Array as GdxArray

class NinjaRunnerGame : Game() {
    @JvmField
    var arrayPerson = GdxArray<Person>()

    @JvmField
    var stage: Stage? = null

    @JvmField
    var batch: SpriteBatch? = null

    @JvmField
    var languages: I18NBundle? = null

    override fun create() {
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
        batch = SpriteBatch()
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))

        Settings.load()
        Assets.load()

        setScreen(GameScreen(this, true))
    }
}
