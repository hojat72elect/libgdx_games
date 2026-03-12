package ca.hojat.sokoban

import ca.hojat.sokoban.screens.MainMenuScreen
import ca.hojat.sokoban.screens.Screens
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport

class SokobanGame : Game() {

    var stage: Stage? = null
    var batch: SpriteBatch? = null
    var languages: I18NBundle? = null

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT))
        batch = SpriteBatch()

        Assets.load()
        Settings.load()

        setScreen(MainMenuScreen(this))
    }
}