package ca.hojat.sokoban.game

import ca.hojat.sokoban.Assets
import ca.hojat.sokoban.Settings
import ca.hojat.sokoban.Settings.levelCompeted
import ca.hojat.sokoban.SokobanGame
import ca.hojat.sokoban.scene2d.CounterTable
import ca.hojat.sokoban.scene2d.TouchPadControlsTable
import ca.hojat.sokoban.scene2d.WindowGroupPause
import ca.hojat.sokoban.screens.MainMenuScreen
import ca.hojat.sokoban.screens.Screens
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport

class GameScreen(game: SokobanGame, var level: Int) : Screens(game) {
    private var state = 0

    private var renderer = BoardRenderer()
    var gameBoard = GameBoard()

    private var touchpadControls = TouchPadControlsTable(this)
    private var buttonUndo: Button
    private var buttonPause: Button

    private var counterTableTime = CounterTable(Assets.backgroundTime!!, 5f, 430f)
    private var counterTableMoves = CounterTable(Assets.backgroundMoves!!, 5f, 380f)

    private val gameStage = Stage(StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT))

    private var windowPause = WindowGroupPause(this)

    init {
        val labelLevelNumber = Label("Level " + (level + 1), LabelStyle(Assets.fontRed, Color.WHITE))
        labelLevelNumber.width = counterTableTime.width
        labelLevelNumber.setPosition(5f, 330f)
        labelLevelNumber.setAlignment(Align.center)

        buttonUndo = Button(Assets.buttonRefreshDrawable, Assets.buttonRefreshPressedDrawable)
        buttonUndo.setSize(80f, 80f)
        buttonUndo.setPosition(700f, 20f)
        buttonUndo.color.a = touchpadControls.color.a // which means that they will have the same alpha color.
        buttonUndo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameBoard.undo = true
            }
        })

        buttonPause = Button(Assets.buttonPauseDrawable, Assets.buttonPausePressedDrawable)
        buttonPause.setSize(60f, 60f)
        buttonPause.setPosition(730f, 410f)
        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPause()
            }
        })

        gameStage.addActor(gameBoard)
        gameStage.addActor(counterTableTime)
        gameStage.addActor(counterTableMoves)
        stage!!.addActor(labelLevelNumber)
        stage!!.addActor(touchpadControls)
        stage!!.addActor(buttonUndo)
        stage!!.addActor(buttonPause)

        setRunning()
    }

    override fun draw(delta: Float) {
        Assets.background!!.render(delta)

        // Render the tileMap
        renderer.render()

        // Render the board
        gameStage.draw()
    }

    override fun update(delta: Float) {
        if (state != STATE_PAUSED) {
            gameStage.act(delta)
            counterTableMoves.updateDisplayedNumber(gameBoard.moves)
            counterTableTime.updateDisplayedNumber(gameBoard.time.toInt())

            if (state == STATE_RUNNING && gameBoard.state == GameBoard.STATE_GAME_OVER) {
                setGameOver()
            }
        }
    }

    private fun setGameOver() {
        state = STATE_GAME_OVER
        levelCompeted(level, gameBoard.moves, gameBoard.time.toInt())
        stage!!.addAction(Actions.sequence(Actions.delay(.35f), Actions.run {
            level += 1
            if (level >= Settings.NUM_MAPS) changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            else changeScreenWithFadeOut(GameScreen::class.java, level, game)
        }))
    }

    private fun setRunning() {
        if (state != STATE_GAME_OVER) {
            state = STATE_RUNNING
        }
    }

    private fun setPause() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            windowPause.show(stage!!)
        }
    }

    override fun up() {
        gameBoard.moveUp = true
        super.up()
    }

    override fun down() {
        gameBoard.moveDown = true
        super.down()
    }

    override fun right() {
        gameBoard.moveRight = true
        super.right()
    }

    override fun left() {
        gameBoard.moveLeft = true
        super.left()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (state == STATE_RUNNING) {
            when (keycode) {
                Input.Keys.LEFT, Input.Keys.A -> {
                    gameBoard.moveLeft = true
                }

                Input.Keys.RIGHT, Input.Keys.D -> {
                    gameBoard.moveRight = true
                }

                Input.Keys.UP, Input.Keys.W -> {
                    gameBoard.moveUp = true
                }

                Input.Keys.DOWN, Input.Keys.S -> {
                    gameBoard.moveDown = true
                }

                Input.Keys.Z -> {
                    gameBoard.undo = true
                }

                Input.Keys.ESCAPE, Input.Keys.BACK -> {
                    setPause()
                }
            }
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (windowPause.isShown) windowPause.hide()
        }

        return true
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_PAUSED: Int = 1
        const val STATE_GAME_OVER: Int = 2
    }
}
