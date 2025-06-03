package com.nopalsoft.sokoban.game

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
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.Settings.levelCompeted
import com.nopalsoft.sokoban.SokobanGame
import com.nopalsoft.sokoban.scene2d.CounterTable
import com.nopalsoft.sokoban.scene2d.TouchPadControlsTable
import com.nopalsoft.sokoban.scene2d.WindowGroupPause
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class GameScreen(game: SokobanGame, var level: Int) : Screens(game) {
    var state: Int = 0

    var renderer: BoardRenderer = BoardRenderer(batch!!)
    var gameBoard: GameBoard = GameBoard()

    var touchpadControls: TouchPadControlsTable = TouchPadControlsTable(this)
    var buttonUndo: Button
    var buttonPause: Button

    var counterTableTime: CounterTable = CounterTable(Assets.backgroundTime!!, 5f, 430f)
    var counterTableMoves: CounterTable = CounterTable(Assets.backgroundMoves!!, 5f, 380f)

    private val gameStage = Stage(StretchViewport(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()))

    var windowPause: WindowGroupPause = WindowGroupPause(this)

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

    fun setRunning() {
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
            if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                gameBoard.moveLeft = true
            } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                gameBoard.moveRight = true
            } else if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                gameBoard.moveUp = true
            } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                gameBoard.moveDown = true
            } else if (keycode == Input.Keys.Z) {
                gameBoard.undo = true
            } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                setPause()
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
