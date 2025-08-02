package com.salvai.snake.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.salvai.snake.SnakeIt
import com.salvai.snake.enums.GameState
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.input.GameGestureDetector
import com.salvai.snake.input.GameInputProcessor
import com.salvai.snake.screens.helper.SpeedChooser
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.Constants.BACKGROUND_COLOR
import com.salvai.snake.utils.GameFlowManager

/**
 * Created by mert on 1/30/18.
 */
class GameScreen(var game: SnakeIt) : ScreenAdapter() {
    private val gameFlowManager: GameFlowManager
    private val gameOverStage: Stage
    private val speedChooser: SpeedChooser
    var gameOver: Boolean
    var gameOverDialogToShow: Boolean
    var newHighscore: Boolean
    var userDirections: Array<MovingDirection?>

    private var updateCountdown: Int
    private var gameOverEffectCount: Int
    private var scoreLabel: Label? = null
    private var scoreContainer: Container<Label?>? = null
    private var handImage: Image? = null


    init {
        game.gameState = GameState.RUNNING
        game.score = 0
        gameOverEffectCount = 8

        speedChooser = SpeedChooser(game)

        gameOverDialogToShow = true
        newHighscore = false

        game.stage!!.clear()
        gameOverStage = Stage(game.viewport)

        game.savePreferences()

        gameOver = false

        updateCountdown = game.worldTime

        gameFlowManager = GameFlowManager(this)
        userDirections = Array<MovingDirection?>()

        addActors()
        setUpTopBar()


        if (game.firstTimeOpen) setUpTutorial()

        setUpInputMultiplexer()

        game.stage!!.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
    }

    override fun render(delta: Float) {
        setupScreen()
        game.draw(delta, gameFlowManager.snake!!.snakeHead.direction, newHighscore)

        if (game.gameState == GameState.STARTED) {
            if (updateCountdown <= 0) if (gameOver) gameOver()
            else updateLogic()
            updateCountdown = (updateCountdown - delta).toInt()
        }

        gameOverStage.act()
        gameOverStage.draw()
    }

    private fun setUpTutorial() {
        handImage = Image(game.assetsManager!!.manager.get<Texture?>(Constants.HAND_IMAGE_NAME, Texture::class.java))
        handImage!!.setBounds(game.worldWidth * 0.5f - 250, game.worldHeight * 0.5f - 150, 300f, 300f)
        handImage!!.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.moveBy(450f, 0f, 1f, Interpolation.pow2In), Actions.fadeOut(Constants.FADE_TIME), Actions.moveBy(-450f, 0f), Actions.delay(
                        Constants.FADE_TIME * 6
                    ), Actions.fadeIn(Constants.FADE_TIME)
                )
            )
        )
        game.stage!!.addActor(handImage)
    }

    fun stopTutorial() {
        val removeAction: Action = object : Action() {
            override fun act(delta: Float): Boolean {
                handImage!!.remove()
                return true
            }
        }

        handImage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME * 2), removeAction))
    }

    private fun setUpGameOverTable() {
        val gameOverTable = Table(game.skin)

        val replayButton = Button(game.skin, "play")
        replayButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameOverStage.addAction(Actions.fadeOut(Constants.FADE_TIME))
                game.stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.setScreen(GameScreen(game))
                        dispose()
                    }
                })))
            }
        })


        val homeButton = Button(game.skin, "home")
        homeButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameOverStage.addAction(Actions.fadeOut(Constants.FADE_TIME))
                game.stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                    override fun run() {
                        game.setScreen(LevelChooseScreen(game))
                        dispose()
                    }
                })))
            }
        })


        gameOverTable.setSize(game.worldWidth, game.worldHeight * 0.8f)
        gameOverTable.setPosition(0f, game.worldHeight * 0.1f)

        gameOverTable.add<Button?>(replayButton).spaceBottom(game.worldWidth * 0.15f).size(game.worldHeight * 0.35f).colspan(3)
        gameOverTable.row().spaceBottom(game.worldWidth * 0.1f)
        gameOverTable.add<Slider?>(speedChooser.slider).colspan(3).width(game.worldWidth * 0.45f).height(game.worldHeight * 0.08f)
        gameOverTable.row()
        gameOverTable.add<Button?>(homeButton).size(Constants.DIALOG_BUTTON_SIZE * 0.7f).colspan(3)

        gameOverTable.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(Constants.FADE_TIME)))
        gameOverStage.addActor(gameOverTable)
    }

    private fun setUpInputMultiplexer() {
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(GameInputProcessor(this))
        inputMultiplexer.addProcessor(GestureDetector(GameGestureDetector(this)))
        inputMultiplexer.addProcessor(game.stage)
        inputMultiplexer.addProcessor(gameOverStage)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    private fun addActors() {
        for (block in gameFlowManager.baseBlocks!!) game.stage!!.addActor(block)
        for (block in gameFlowManager.blocks) game.stage!!.addActor(block)
        game.stage!!.addActor(gameFlowManager.apple)
        game.stage!!.addActor(gameFlowManager.snake!!.snakeHead)
    }


    fun updateLogic() {
        val newBody = gameFlowManager.update(if (userDirections.size > 0) userDirections.first() else null)
        if (newBody != null) game.stage!!.addActor(newBody)

        if (userDirections.size > 0) userDirections.removeIndex(0)

        updateCountdown = game.worldTime
    }


    private fun gameOver() {
        if (gameOverEffectCount > 0) {
            if (gameOverEffectCount % 2 == 0) gameFlowManager.snake!!.hide()
            else gameFlowManager.snake!!.show()
            gameOverEffectCount--
        } else if (game.firstTimeOpen) {
            game.firstTimeOpen = false
            gameOverDialogToShow = false
            game.savePreferences()
            gameOverStage.addAction(Actions.fadeOut(Constants.FADE_TIME))
            game.stage!!.addAction(Actions.sequence(Actions.fadeOut(Constants.FADE_TIME), Actions.run(object : Runnable {
                override fun run() {
                    game.setScreen(LevelChooseScreen(game))
                    dispose()
                }
            })))
        } else if (gameOverDialogToShow) {
            if (newHighscore) {
                scoreContainer!!.addAction(
                    Actions.forever(
                        Actions.sequence(
                            Actions.scaleBy(
                                Constants.SCORE_SCALE, Constants.SCORE_SCALE, Constants.HIGH_SCORE_DURATION,
                                Constants.INTERPOLATION
                            ), Actions.scaleBy(-Constants.SCORE_SCALE, -Constants.SCORE_SCALE, Constants.HIGH_SCORE_DURATION, Constants.INTERPOLATION)
                        )
                    )
                )
            }

            setUpGameOverTable()
            gameOverDialogToShow = false
        }

        updateCountdown = Constants.WORLD_GAME_OVER_TIME
    }


    private fun setupScreen() {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.camera!!.update()
    }


    private fun setUpTopBar() {
        scoreLabel = Label("0", game.skin, "default")
        scoreLabel!!.setAlignment(Align.right)
        scoreLabel!!.setFontScale(2f)

        scoreContainer = Container<Label?>(scoreLabel)
        scoreContainer!!.isTransform = true
        scoreContainer!!.setSize(game.worldWidth * 0.2f, game.worldWidth * 0.1f)
        scoreContainer!!.setOrigin(scoreContainer!!.getWidth() / 2, scoreContainer!!.getHeight() / 2)
        scoreContainer!!.setPosition(game.worldWidth * 0.5f - scoreContainer!!.getWidth() * 0.5f, game.worldHeight - game.worldWidth * 0.15f)
        game.stage!!.addActor(scoreContainer)
    }

    fun updateScoreLabel() {
        scoreContainer!!.addAction(Actions.sequence(Actions.scaleBy(Constants.SCORE_SCALE, Constants.SCORE_SCALE, Constants.SCORE_DURATION, Constants.INTERPOLATION), Actions.run(object : Runnable {
            override fun run() {
                scoreLabel!!.setText("" + game.score)
            }
        }), Actions.scaleBy(-Constants.SCORE_SCALE, -Constants.SCORE_SCALE, Constants.SCORE_DURATION, Constants.INTERPOLATION)))
    }


    override fun pause() {
        game.savePreferences()
    }

    override fun resize(width: Int, height: Int) {
        game.viewport!!.update(width, height, true)
    }

    override fun resume() {
        game.getPreferences()
        Gdx.input.isCatchBackKey = true
    }
}
