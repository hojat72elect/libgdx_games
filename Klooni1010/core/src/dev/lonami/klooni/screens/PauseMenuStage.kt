package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.actors.Band
import dev.lonami.klooni.actors.SoftButton
import dev.lonami.klooni.game.BaseScorer
import dev.lonami.klooni.game.GameLayout

// The pause stage is not a whole screen but rather a menu
// which can be overlaid on top of another screen
internal class PauseMenuStage(layout: GameLayout, private val game: Klooni, private val scorer: BaseScorer, gameMode: Int) : Stage() {
    private val shapeRenderer: ShapeRenderer = ShapeRenderer(20) // 20 vertex seems to be enough for a rectangle
    private val band: Band
    private val playButton: SoftButton
    private val customButton: SoftButton // Customize & "Shut down"
    private val customChangeListener: ChangeListener = object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            // Don't dispose because then it needs to take us to the previous screen
            game.transitionTo(CustomizeScreen(game, game.getScreen()), false)
        }
    }
    private var lastInputProcessor: InputProcessor? = null
    var isShown: Boolean = false
        private set

    var isHiding: Boolean = false
        private set

    private val playChangeListener: ChangeListener = object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            hide()
        }
    }

    // We need the score to save the maximum score if a new record was beaten
    init {

        val table = Table()
        table.setFillParent(true)
        addActor(table)

        // Current and maximum score band.
        // Do not add it to the table not to over-complicate things.
        band = Band(game, layout, this.scorer)
        addActor(band)

        // Home screen button
        val homeButton = SoftButton(3, "home_texture")
        table.add<SoftButton?>(homeButton).space(16f)

        homeButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.transitionTo(MainMenuScreen(game))
            }
        })

        // Replay button
        val replayButton = SoftButton(0, "replay_texture")
        table.add<SoftButton?>(replayButton).space(16f)

        replayButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                // false, don't load the saved game state; we do want to replay
                game.transitionTo(GameScreen(game, gameMode, false))
            }
        })

        table.row()

        // Palette button OR shutdown game (if game over)
        customButton = SoftButton(1, "palette_texture")
        table.add<SoftButton?>(customButton).space(16f)
        customButton.addListener(customChangeListener)

        // Continue playing OR share (if game over) button
        playButton = SoftButton(2, "play_texture")
        table.add<SoftButton?>(playButton).space(16f)
        playButton.addListener(playChangeListener)
    }

    // Hides the pause menu, setting back the previous input processor
    private fun hide() {
        this.isShown = false
        this.isHiding = true
        Gdx.input.inputProcessor = lastInputProcessor

        addAction(
            Actions.sequence(
                Actions.moveTo(0f, Gdx.graphics.height.toFloat(), 0.5f, Interpolation.swingIn),
                object : RunnableAction() {
                    override fun run() {
                        this@PauseMenuStage.isHiding = false
                    }
                }
            ))
        scorer.resume()
    }

    // Shows the pause menu, indicating whether it's game over or not
    fun show() {
        scorer.pause()
        scorer.saveScore()

        // Save the last input processor so then we can return the handle to it
        lastInputProcessor = Gdx.input.inputProcessor
        Gdx.input.inputProcessor = this
        this.isShown = true
        this.isHiding = false

        addAction(Actions.moveTo(0f, Gdx.graphics.height.toFloat()))
        addAction(Actions.moveTo(0f, 0f, 0.75f, Interpolation.swingOut))
    }

    fun showGameOver(gameOverReason: String, timeMode: Boolean) {
        // Allow the players to exit the game
        customButton.removeListener(customChangeListener)
        customButton.updateImage("power_off_texture")
        customButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.app.exit()
            }
        })

        if (game.shareChallenge != null) {
            playButton.removeListener(playChangeListener)
            playButton.updateImage("share_texture")
            playButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    // Don't dispose because then it needs to take us to the previous screen
                    game.transitionTo(
                        ShareScoreScreen(
                            game, game.getScreen(), scorer.getCurrentScore(), timeMode
                        ), false
                    )
                }
            })
        }

        band.setMessage(gameOverReason)
        show()
    }

    override fun draw() {
        if (this.isShown) {
            // Draw an overlay rectangle with not all the opacity
            // This is the only place where ShapeRenderer is OK because the batch hasn't started
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            val color = Color(Klooni.theme!!.bandColor)
            color.a = 0.1f
            shapeRenderer.color = color
            shapeRenderer.rect(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            shapeRenderer.end()
        }

        super.draw()
    }

    override fun keyUp(keyCode: Int): Boolean {
        if (keyCode == Input.Keys.P || keyCode == Input.Keys.BACK)  // Pause
            hide()

        return super.keyUp(keyCode)
    }
}
