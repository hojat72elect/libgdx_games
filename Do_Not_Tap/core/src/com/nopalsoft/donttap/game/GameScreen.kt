package com.nopalsoft.donttap.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.DoNotTapGame
import com.nopalsoft.donttap.Settings
import com.nopalsoft.donttap.dialogs.GameOverDialog
import com.nopalsoft.donttap.dialogs.GamePausedDialog
import com.nopalsoft.donttap.screens.Screens

class GameScreen(game: DoNotTapGame, @JvmField var mode: Int) : Screens(game) {
    var state: Int

    private val stageGame: Stage

    @JvmField
    var oWorld: WorldGame

    var lbTime: Label? = null
    var lbTilesScore: Label? = null
    var lbBestScore: Label? = null
    var tbMarcadores: Table? = null

    init {
        stageGame = Stage(StretchViewport(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()))

        oWorld = WorldGame(mode)
        stageGame.addActor(oWorld)

        initMarcadores()

        // I need to add the stageGame to the events.
        val input = InputMultiplexer(this, stage, stageGame)
        Gdx.input.inputProcessor = input

        state = STATE_RUNNING
        Settings.numeroVecesJugadas++
    }

    private fun initMarcadores() {
        tbMarcadores = Table()
        tbMarcadores!!.setSize(SCREEN_WIDTH.toFloat(), 80f)
        tbMarcadores!!.setPosition(0f, SCREEN_HEIGHT - tbMarcadores!!.getHeight())

        val background = Image(Assets.tileBlanco)
        background.setSize(tbMarcadores!!.getWidth(), tbMarcadores!!.getHeight())
        background.setPosition(0f, 0f)
        tbMarcadores!!.addActor(background)

        lbTime = Label("Time\n0.0", Assets.labelStyleBlack)
        lbTime!!.setAlignment(Align.center)
        lbTime!!.setFontScale(.9f)

        lbTilesScore = Label("Tiles\n0", Assets.labelStyleBlack)
        lbTilesScore!!.setAlignment(Align.center)
        lbTilesScore!!.setFontScale(.85f)

        lbBestScore = Label("Best\n0", Assets.labelStyleBlack)
        lbBestScore!!.setAlignment(Align.center)
        lbBestScore!!.setFontScale(.8f)

        when (mode) {
            MODE_CLASSIC -> {
                var text = game?.formatter!!.format("%.1f", Settings.bestTimeClassicMode)
                if (Settings.bestTimeClassicMode >= 100100) text = "X"
                lbBestScore!!.setText("Best\n" + text)
            }

            MODE_TIME -> lbBestScore!!.setText("Best\n" + Settings.bestScoreTimeMode)
            MODE_ENDLESS -> lbBestScore!!.setText("Best\n" + Settings.bestScoreEndlessMode)
        }

        tbMarcadores!!.row().expand().uniform().center()
        tbMarcadores!!.add<Label?>(lbTime)
        tbMarcadores!!.add<Label?>(lbTilesScore)
        tbMarcadores!!.add<Label?>(lbBestScore)

        stage.addActor(tbMarcadores)
    }

    override fun update(delta: Float) {
        if (state == STATE_RUNNING) {
            stageGame.act(delta)

            if (oWorld.state == WorldGame.STATE_GAME_OVER_2) {
                if (mode == MODE_ENDLESS) setWin()
                else setGameover()
            } else if (oWorld.state == WorldGame.STATE_GAME_WIN) {
                setWin()
            }
        }

        lbTime!!.setText("Time\n" + game?.formatter!!.format("%.1f", oWorld.time))
        lbTilesScore!!.setText("Tiles\n" + oWorld.score)
    }

    // The difference if you win is that your scores go up;
    private fun setWin() {
        when (mode) {
            MODE_CLASSIC -> if (Settings.bestTimeClassicMode > oWorld.time) Settings.bestTimeClassicMode = oWorld.time
            MODE_TIME -> if (Settings.bestScoreTimeMode < oWorld.score) Settings.bestScoreTimeMode = oWorld.score
            MODE_ENDLESS -> if (Settings.bestScoreEndlessMode < oWorld.score) Settings.bestScoreEndlessMode = oWorld.score
        }

        setGameover()
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        val ventana = GameOverDialog(this)
        ventana.show(stage)
    }

    fun setRunning() {
        state = STATE_RUNNING
    }

    fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            val oPaused = GamePausedDialog(this)
            oPaused.show(stage)
        }
    }

    override fun draw(delta: Float) {
        stageGame.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stageGame.viewport.update(width, height, true)
    }

    override fun pause() {
        setPaused()
        super.pause()
    }

    override fun hide() {
        super.hide()
        stageGame.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.S) oWorld.moveRowsDown()
        else if (keycode == Input.Keys.W) oWorld.moveRowsUp()
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) setPaused()
        return super.keyDown(keycode)
    }

    companion object {
        const val STATE_RUNNING: Int = 2
        const val STATE_PAUSED: Int = 3
        const val STATE_GAME_OVER: Int = 4
        const val MODE_CLASSIC: Int = 0 // Record 50 tiles in the shortest time
        const val MODE_TIME: Int = 1 // Walk for 1 minute
        const val MODE_ENDLESS: Int = 2 // Don't let any tile go by
    }
}
