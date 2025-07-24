package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.actors.SoftButton
import kotlin.math.min

// Main menu screen, presenting some options (play, customize…)
class MainMenuScreen(private val game: Klooni) : InputListener(), Screen {
    private val stage: Stage

    init {
        stage = Stage()

        val table = Table()
        table.setFillParent(true)
        stage.addActor(table)

        // Play button
        val playButton = SoftButton(
            0, if (GameScreen.hasSavedData()) "play_saved_texture" else "play_texture"
        )
        playButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                this@MainMenuScreen.game.transitionTo(
                    GameScreen(this@MainMenuScreen.game, GameScreen.GAME_MODE_SCORE)
                )
            }
        })
        table.add<SoftButton?>(playButton).colspan(3).fill().space(16f)

        table.row()

        // Star button (on GitHub)
        val starButton = SoftButton(1, "star_texture")
        starButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.net.openURI("https://github.com/LonamiWebs/Klooni1010/stargazers")
            }
        })
        table.add<SoftButton?>(starButton).space(16f)

        // Time mode
        val statsButton = SoftButton(2, "stopwatch_texture")
        statsButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                this@MainMenuScreen.game.transitionTo(
                    GameScreen(this@MainMenuScreen.game, GameScreen.GAME_MODE_TIME)
                )
            }
        })
        table.add<SoftButton?>(statsButton).space(16f)

        // Palette button (buy colors)
        val paletteButton = SoftButton(3, "palette_texture")
        paletteButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                // Don't dispose because then it needs to take us to the previous screen
                this@MainMenuScreen.game.transitionTo(
                    CustomizeScreen(
                        this@MainMenuScreen.game, this@MainMenuScreen.game.getScreen()
                    ), false
                )
            }
        })
        table.add<SoftButton?>(paletteButton).space(16f)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Klooni.theme!!.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(min(Gdx.graphics.deltaTime, minDelta))
        stage.draw()

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit()
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    companion object {
        // As the examples show on the LibGdx wiki
        private val minDelta = 1 / 30f
    }
}
