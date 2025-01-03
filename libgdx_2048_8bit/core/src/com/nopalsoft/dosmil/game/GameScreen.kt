package com.nopalsoft.dosmil.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.MainGame
import com.nopalsoft.dosmil.Settings
import com.nopalsoft.dosmil.Settings.setBestScores
import com.nopalsoft.dosmil.scene2d.ScreenGameOver
import com.nopalsoft.dosmil.scene2d.ScreenPaused
import com.nopalsoft.dosmil.screens.MainMenuScreen
import com.nopalsoft.dosmil.screens.Screens

class GameScreen(game: MainGame) : Screens(game) {

    private var state: Int = 0
    private var board: Board = Board()
    private var tableMarkers = Table()
    private var labelTime: Label? = null
    private var labelScore: Label? = null
    private var labelBestScore: Label? = null
    private var buttonPause: Button? = null
    private var screenPaused: ScreenPaused? = null
    private val stageGame = Stage(StretchViewport(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()))

    init {
        stageGame.addActor(board)


        tableMarkers.setSize(SCREEN_WIDTH.toFloat(), 100f)
        tableMarkers.setPosition(0f, 680f)


        labelTime = Label("${Assets.languages!!["score"]}0", Assets.labelStyleSmall)
        labelTime!!.setAlignment(Align.center)
        labelTime!!.setFontScale(1.15f)

        labelScore = Label(
            """
                ${Assets.languages!!["score"]}
                0
                """.trimIndent(), Assets.labelStyleSmall
        )
        labelScore!!.setFontScale(1.15f)
        labelScore!!.setAlignment(Align.center)

        labelBestScore = Label(
            """
                ${Assets.languages!!["best"]}
                ${Settings.bestScore}
                """.trimIndent(), Assets.labelStyleSmall
        )
        labelBestScore!!.setAlignment(Align.center)
        labelBestScore!!.setFontScale(1.15f)

        tableMarkers.row().expand().uniform().center()
        tableMarkers.add(labelTime)
        tableMarkers.add(labelScore)
        tableMarkers.add(labelBestScore)

        screenPaused = ScreenPaused(this)

        buttonPause = Button(Assets.buttonStylePause)
        buttonPause!!.setPosition(SCREEN_WIDTH.toFloat() / 2 - buttonPause!!.width / 2, 110f)
        buttonPause!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPaused()
            }
        })

        stage!!.addActor(tableMarkers)
        stage!!.addActor(buttonPause)

        Settings.numberTimesPlayed++
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stageGame.viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
        if (state == STATE_RUNNING) {
            updateRunning(delta)
        }

        labelTime!!.setText(
            """
                ${Assets.languages!!["time"]}
                ${board.time.toInt()}
                """.trimIndent()
        )
        labelScore!!.setText(
            """
                ${Assets.languages!!["score"]}
                ${board.score}
                """.trimIndent()
        )
    }

    private fun updateRunning(delta: Float) {
        stageGame.act(delta)

        if (board.state == Board.STATE_GAME_OVER) {
            setGameOver()
        }
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
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

    private fun setPaused() {
        if (state == STATE_GAME_OVER || state == STATE_PAUSED) return
        state = STATE_PAUSED
        stage!!.addActor(screenPaused)
    }

    fun setRunning() {
        if (state == STATE_GAME_OVER) return
        state = STATE_RUNNING
    }

    private fun setGameOver() {
        state = STATE_GAME_OVER
        setBestScores(board.score)
        val oGameOver = ScreenGameOver(this, board.didWin, board.time.toInt(), board.score)
        stage!!.addActor(oGameOver)
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        if (state != STATE_PAUSED) setRunning()
        return super.fling(velocityX, velocityY, button)
    }

    /**
     * It's important to remember that it is the white piece that moves, so if I say moveUp the white piece will move up, but the user thinks
     * that if he does a swipe down the piece above the white piece is the one that goes down. When we know that the one that goes up is the white piece.
     */
    override fun up() {
        board.moveUp = true
        super.up()
    }

    override fun down() {
        board.moveDown = true
        super.down()
    }

    override fun right() {
        board.moveRight = true
        super.right()
    }

    override fun left() {
        board.moveLeft = true
        super.left()
    }

    /**
     * It's important to remember that it is the white piece that moves, so if I say moveUp the white piece will move up, but the user thinks that if he does
     * a swipe down the piece above the white piece is the one that goes down. When we know that the one that goes up is the white piece.
     */
    override fun keyDown(keycode: Int): Boolean {
        if (state != STATE_PAUSED) {
            when (keycode) {
                Input.Keys.LEFT -> {
                    board.moveLeft = true
                    setRunning()
                }

                Input.Keys.RIGHT -> {
                    board.moveRight = true
                    setRunning()
                }

                Input.Keys.UP -> {
                    board.moveUp = true
                    setRunning()
                }

                Input.Keys.DOWN -> {
                    board.moveDown = true

                    setRunning()
                }
            }
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game)
        }

        return true
    }

    companion object {
        const val STATE_RUNNING: Int = 1
        const val STATE_PAUSED: Int = 2
        const val STATE_GAME_OVER: Int = 3
    }
}
