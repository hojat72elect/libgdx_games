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
import com.nopalsoft.sokoban.MainSokoban
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.scene2d.CounterTable
import com.nopalsoft.sokoban.scene2d.DialogPause
import com.nopalsoft.sokoban.scene2d.OnScreenGamePad
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.BaseScreen

class GameScreen(game: MainSokoban, var level: Int) : BaseScreen(game) {

    private var state = 0

    private val boardRenderer = BoardRenderer(batcher!!)
    private val board = Board()
    private val oControl = OnScreenGamePad(this)
    private val buttonUndo = Button(Assets.btRefresh, Assets.btRefreshPress)
    private val buttonPause = Button(Assets.btPausa, Assets.btPausaPress)
    private val barTime = CounterTable(Assets.backgroundTime, 5F, 430F)
    private val barMoves = CounterTable(Assets.backgroundMoves, 5F, 380F)
    private val dialogPause = DialogPause(this)
    private val stageGame = Stage(StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT))

    init {
        val labelLevel = Label("Level " + (level + 1), LabelStyle(Assets.fontRed, Color.WHITE))
        labelLevel.width = barTime.width
        labelLevel.setPosition(5f, 330f)
        labelLevel.setAlignment(Align.center)

        buttonUndo.setSize(80f, 80f)
        buttonUndo.setPosition(700f, 20f)
        buttonUndo.color.a = oControl.color.a // That they have the same color of alpha
        buttonUndo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                board.undo = true
            }
        })

        buttonPause.setSize(60f, 60f)
        buttonPause.setPosition(730f, 410f)
        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPause()
            }
        })

        stageGame.addActor(board)
        stageGame.addActor(barTime)
        stageGame.addActor(barMoves)
        stage?.addActor(labelLevel)
        stage?.addActor(oControl)
        stage?.addActor(buttonUndo)
        stage?.addActor(buttonPause)

        setRunning()
    }

    override fun draw(delta: Float) {

        Assets.background.render(delta)

        //Render the tileMap
        boardRenderer.render()

        // Render the board
        stageGame.draw()
    }

    override fun update(delta: Float) {
        if (state != STATE_PAUSED) {
            stageGame.act(delta)
            barMoves.updateActualNum(board.moves)
            barTime.updateActualNum(board.time.toInt())

            if (state == STATE_RUNNING && board.state == Board.STATE_GAME_OVER) {
                setGameOver()
            }
        }
    }

    private fun setGameOver() {
        state = STATE_GAME_OVER
        Settings.levelCompleted(level, board.moves, board.time.toInt())
        stage?.addAction(Actions.sequence(Actions.delay(.35f), Actions.run {
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
            dialogPause.show(stage!!)
        }
    }

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

    override fun keyDown(keycode: Int): Boolean {
        if (state == STATE_RUNNING) {
            when (keycode) {
                Input.Keys.LEFT, Input.Keys.A -> {
                    board.moveLeft = true
                }

                Input.Keys.RIGHT, Input.Keys.D -> {
                    board.moveRight = true
                }

                Input.Keys.UP, Input.Keys.W -> {
                    board.moveUp = true
                }

                Input.Keys.DOWN, Input.Keys.S -> {
                    board.moveDown = true
                }

                Input.Keys.Z -> {
                    board.undo = true
                }

                Input.Keys.ESCAPE, Input.Keys.BACK -> {
                    setPause()
                }
            }
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (dialogPause.isShown()) dialogPause.hide()
        }

        return true
    }

    companion object {
        private const val STATE_RUNNING = 0
        private const val STATE_PAUSED = 1
        private const val STATE_GAME_OVER = 2
    }
}