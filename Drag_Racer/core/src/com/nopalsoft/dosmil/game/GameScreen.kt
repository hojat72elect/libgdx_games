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
import com.nopalsoft.dosmil.scene2d.GameOverDialog
import com.nopalsoft.dosmil.scene2d.GamePausedDialog
import com.nopalsoft.dosmil.screens.MainMenuScreen
import com.nopalsoft.dosmil.screens.Screens

class GameScreen(game: MainGame) : Screens(game) {
    private var state: Int = 0

    private var oGameBoard: GameBoard = GameBoard()

    private val stageGame =
        Stage(StretchViewport(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()))

    private var tableBookmarks: Table? = null
    private var labelTime: Label? = null
    private var labelScore: Label? = null
    private var labelBestScore: Label? = null

    private var buttonPause: Button? = null
    private var pausedDialog: GamePausedDialog? = null

    init {
        stageGame.addActor(oGameBoard)

        initUI()

        Settings.numberOfTimesPlayed++
    }

    private fun initUI() {
        tableBookmarks = Table()
        tableBookmarks!!.setSize(SCREEN_WIDTH.toFloat(), 100f)
        tableBookmarks!!.setPosition(0f, 680f)


        labelTime = Label(
            """
                ${Assets.languagesBundle!!["score"]}
                0
                """.trimIndent(), Assets.labelStyleSmall
        )
        labelTime!!.setAlignment(Align.center)
        labelTime!!.setFontScale(1.15f)

        labelScore = Label(
            """
                ${Assets.languagesBundle!!["score"]}
                0
                """.trimIndent(), Assets.labelStyleSmall
        )
        labelScore!!.setFontScale(1.15f)
        labelScore!!.setAlignment(Align.center)

        labelBestScore = Label(
            """
                ${Assets.languagesBundle!!["best"]}
                ${Settings.bestScore}
                """.trimIndent(), Assets.labelStyleSmall
        )
        labelBestScore!!.setAlignment(Align.center)
        labelBestScore!!.setFontScale(1.15f)

        tableBookmarks!!.row().expand().uniform().center()
        tableBookmarks!!.add(labelTime)
        tableBookmarks!!.add(labelScore)
        tableBookmarks!!.add(labelBestScore)

        pausedDialog = GamePausedDialog(this)

        buttonPause = Button(Assets.buttonStylePause)
        buttonPause!!.setPosition(SCREEN_WIDTH / 2f - buttonPause!!.width / 2, 110f)
        buttonPause!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPaused()
            }
        })

        stage!!.addActor(tableBookmarks)
        stage!!.addActor(buttonPause)
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
                ${Assets.languagesBundle!!["time"]}
                ${oGameBoard.elapsedTime.toInt()}
                """.trimIndent()
        )
        labelScore!!.setText(
            """
                ${Assets.languagesBundle!!["score"]}
                ${oGameBoard.score}
                """.trimIndent()
        )
    }

    private fun updateRunning(delta: Float) {
        stageGame.act(delta)

        if (oGameBoard.state == GameBoard.STATE_GAMEOVER) {
            setGameover()
        }
    }

    override fun draw(delta: Float) {
        batch!!.begin()
        batch!!.draw(Assets.backgroundAtlasRegion!!, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch!!.end()

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
        stage!!.addActor(pausedDialog)
    }

    fun setRunning() {
        if (state == STATE_GAME_OVER) return
        state = STATE_RUNNING
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        setBestScores(oGameBoard.score)
        val gameOverDialog = GameOverDialog(this, oGameBoard.didWin, oGameBoard.elapsedTime.toInt(), oGameBoard.score)
        stage!!.addActor(gameOverDialog)
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        if (state != STATE_PAUSED) setRunning()
        return super.fling(velocityX, velocityY, button)
    }

    override fun up() {
        oGameBoard.moveUp = true
        super.up()
    }

    override fun down() {
        oGameBoard.moveDown = true
        super.down()
    }

    override fun right() {
        oGameBoard.moveRight = true
        super.right()
    }

    override fun left() {
        oGameBoard.moveLeft = true
        super.left()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (state != STATE_PAUSED) {
            when (keycode) {
                Input.Keys.LEFT -> {
                    oGameBoard.moveLeft = true
                    setRunning()
                }

                Input.Keys.RIGHT -> {
                    oGameBoard.moveRight = true
                    setRunning()
                }

                Input.Keys.UP -> {
                    oGameBoard.moveUp = true
                    setRunning()
                }

                Input.Keys.DOWN -> {
                    oGameBoard.moveDown = true

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
