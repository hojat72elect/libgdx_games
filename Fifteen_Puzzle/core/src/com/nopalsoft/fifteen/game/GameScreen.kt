package com.nopalsoft.fifteen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.MainFifteen
import com.nopalsoft.fifteen.Settings
import com.nopalsoft.fifteen.Settings.setBestScores
import com.nopalsoft.fifteen.scene2d.MarcoGameOver
import com.nopalsoft.fifteen.scene2d.MarcoPaused
import com.nopalsoft.fifteen.screens.MainMenuScreen
import com.nopalsoft.fifteen.screens.Screens

class GameScreen(game: MainFifteen) : Screens(game) {
    private val stageGame: Stage = Stage(StretchViewport(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()))
    var state: Int = 0
    var oTablero: Tablero = Tablero()
    var tbMarcadores: Table? = null
    var lbTime: Label? = null
    var lbMoves: Label? = null

    var btPause: Button? = null

    var oMarcoPaused: MarcoPaused? = null

    init {
        stageGame.addActor(oTablero)

        initUI()

        setReady()

        Settings.numeroVecesJugadas++
    }

    private fun initUI() {
        tbMarcadores = Table()
        tbMarcadores!!.setSize(SCREEN_WIDTH.toFloat(), 100f)
        tbMarcadores!!.setPosition(0f, 680f)
        lbTime = Label("Time\n0", Assets.labelStyleChico)
        lbTime!!.setAlignment(Align.center)
        lbTime!!.setFontScale(1.15f)
        lbMoves = Label("Moves\n0", Assets.labelStyleChico)
        lbMoves!!.setFontScale(1.15f)
        lbMoves!!.setAlignment(Align.center)

        tbMarcadores!!.row().expand().uniform().center()
        tbMarcadores!!.add<Label?>(lbTime)
        tbMarcadores!!.add<Label?>(lbMoves)

        btPause = Button(Assets.styleButtonPause)
        btPause!!.setPosition(SCREEN_WIDTH / 2f - btPause!!.getWidth() / 2, 110f)
        btPause!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                setPaused()
            }
        })

        oMarcoPaused = MarcoPaused(this)

        stage!!.addActor(tbMarcadores)
        stage!!.addActor(btPause)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stageGame.viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
        if (state == STATE_RUNNING) {
            updateRunning(delta)
        }

        lbTime!!.setText("Time\n" + (oTablero.tiempo.toInt()))
        lbMoves!!.setText("Moves\n" + (oTablero.moves))
    }

    private fun updateRunning(delta: Float) {
        stageGame.act(delta)

        if (oTablero.state == Tablero.STATE_GAMEOVER) {
            setGameover()
        }
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.fondo, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher!!.end()

        stageGame.draw()
    }

    override fun pause() {
        setPaused()
        super.pause()
    }

    override fun hide() {
        super.hide()
        stageGame.dispose()
    }

    private fun setReady() {
        state = STATE_READY
    }

    private fun setPaused() {
        if (state == STATE_GAME_OVER || state == STATE_PAUSED) return
        state = STATE_PAUSED
        stage!!.addActor(oMarcoPaused)
    }

    fun setRunning() {
        if (state == STATE_GAME_OVER) return
        state = STATE_RUNNING
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        setBestScores(oTablero.tiempo.toInt(), oTablero.moves)

        val oMarcoGameover = MarcoGameOver(
            this,
            oTablero.tiempo.toInt(), oTablero.moves
        )
        stage!!.addActor(oMarcoGameover)
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        setRunning()
        return super.fling(velocityX, velocityY, button)
    }

    /**
     * Es muy imporante recordar que lo que se mueve es la pieza blanca por lo tanto si yo digo moveUp la pieza blanca se movera hacia arriba pero el usuario piensa que si hace un swipe down la pieza
     * de arriba de la blanca es la que baja. Cuando nosotros sabemos que la que sube es la blanca.
     */
    override fun up() {
        oTablero.moveDown = true
        super.up()
    }

    override fun down() {
        oTablero.moveUp = true
        super.down()
    }

    override fun right() {
        oTablero.moveLeft = true
        super.right()
    }

    override fun left() {
        oTablero.moveRight = true
        super.left()
    }

    /**
     * Es muy imporante recordar que lo que se mueve es la pieza blanca por lo tanto si yo digo moveUp la pieza blanca se movera hacia arriba pero el usuario piensa que si hace un swipe down la pieza
     * de arriba de la blanca es la que baja. Cuando nosotros sabemos que la que sube es la blanca.
     */
    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT -> {
                oTablero.moveRight = true
                setRunning()
            }

            Input.Keys.RIGHT -> {
                oTablero.moveLeft = true
                setRunning()
            }

            Input.Keys.UP -> {
                oTablero.moveDown = true
                setRunning()
            }

            Input.Keys.DOWN -> {
                oTablero.moveUp = true
                setRunning()
            }

            Input.Keys.ESCAPE, Input.Keys.BACK -> {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
            }

            else -> {}
        }
        return true
    }

    companion object {
        const val STATE_READY: Int = 1
        const val STATE_RUNNING: Int = 2
        const val STATE_PAUSED: Int = 3
        const val STATE_GAME_OVER: Int = 4
    }
}
