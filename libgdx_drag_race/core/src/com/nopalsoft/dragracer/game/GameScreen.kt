package com.nopalsoft.dragracer.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.Settings.setNewScore
import com.nopalsoft.dragracer.game_objects.SpeedBar
import com.nopalsoft.dragracer.scene2D.GameOverGroup
import com.nopalsoft.dragracer.scene2D.SwipeHorizontalTutorial
import com.nopalsoft.dragracer.scene2D.SwipeVerticalTutorial
import com.nopalsoft.dragracer.screens.MainMenuScreen
import com.nopalsoft.dragracer.screens.BaseScreen
import com.nopalsoft.dragracer.shop.ShopScreen

class GameScreen(game: MainStreet?) : BaseScreen(game!!) {


    private lateinit var labelScore: Label
    private lateinit var labelCoin: Label
    private lateinit var tableScores: Table
    private lateinit var labelTryAgain: Label
    private lateinit var labelShopScreen: Label
    private lateinit var labelLeaderboard: Label
    private lateinit var speedBar: SpeedBar
    private var score = 0
    private var coins = 0
    private var canSuperSpeed = false
    private lateinit var groupPaused: Group
    private lateinit var gameOverGroup: GameOverGroup
    lateinit var buttonMusic: Button
    private val stageGame = Stage(StretchViewport(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()))
    private val trafficGame = TrafficGame()

    init {
        stageGame.addActor(trafficGame)
        initUI()
        setReady()
        Settings.numberOfTimesPlayed++
    }

    private fun initUI() {
        speedBar = SpeedBar(TrafficGame.NUM_COINS_FOR_SUPER_SPEED.toFloat(), 5f, 720f, 160f, 20f)

        labelScore = Label("Distance 0m", Assets.labelStyleLarge)
        labelScore.setFontScale(.8f)

        labelCoin = Label("0", Assets.labelStyleLarge)
        labelCoin.setFontScale(.8f)

        val imageCoinFront = Image(Assets.coinFront)

        tableScores = Table()
        tableScores.width = SCREEN_WIDTH.toFloat()
        tableScores.setPosition(0f, SCREEN_HEIGHT - labelScore.height / 2)
        tableScores.padLeft(5f).padRight(5f)
        tableScores.add(labelScore).left()
        tableScores.add(labelCoin).right().expand().padRight(5f)
        tableScores.add(imageCoinFront).right()

        // Gameover
        labelTryAgain = Label("Try again", Assets.labelStyleLarge)
        labelTryAgain.setPosition(500f, 310f)
        labelTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        labelShopScreen = Label("Shop screen", Assets.labelStyleLarge)
        labelShopScreen.setPosition(500f, 210f)
        labelShopScreen.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(ShopScreen::class.java, game)
            }
        })

        labelLeaderboard = Label("Leaderboard", Assets.labelStyleLarge)
        labelLeaderboard.setPosition(500f, 110f)
        labelLeaderboard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {

            }
        })

        buttonMusic = Button(Assets.styleButtonMusic)
        buttonMusic.setPosition(5f, 5f)
        buttonMusic.isChecked = !Settings.isMusicOn
        Gdx.app.log("Musica", Settings.isMusicOn.toString() + "")
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic.isChecked = !Settings.isMusicOn
                if (Settings.isMusicOn) Assets.music.play()
                else Assets.music.stop()
                super.clicked(event, x, y)
            }
        })

        entranceAction(labelTryAgain, 310f, .25f)
        entranceAction(labelShopScreen, 210f, .5f)
        entranceAction(labelLeaderboard, 110f, .85f)

        setAnimationChangeColor(labelShopScreen)
        setAnimationChangeColor(labelLeaderboard)
        setAnimationChangeColor(labelTryAgain)

        // Paused
        groupPaused = Group()
        groupPaused.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())

        val background = Image(Assets.pixelBlack)
        background.setSize(groupPaused.width, groupPaused.height)

        val labelPaused = Label("Game Paused\nTouch to resume", Assets.labelStyleLarge)
        labelPaused.setPosition(
            groupPaused.width / 2f - labelPaused.width / 2f,
            groupPaused.height / 2f - labelPaused.height / 2f
        )
        labelPaused.setAlignment(Align.center)
        labelPaused.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.alpha(.55f, .85f),
                    Actions.alpha(1f, .85f)
                )
            )
        )
        groupPaused.addActor(labelPaused)
        groupPaused.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setRunning()
            }
        })
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stageGame.viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
        if (state == STATE_RUNNING) {
            updateRunning(delta)
        }
    }

    private fun updateRunning(delta: Float) {
        stageGame.act(delta)

        score = trafficGame.score.toInt()
        coins = trafficGame.coins
        if (trafficGame.state == TrafficGame.STATE_GAME_OVER) {
            setGameover()
        }

        if (!canSuperSpeed && trafficGame.canSuperSpeed) {
            canSuperSpeed = true
            SwipeVerticalTutorial(stage)
        }

        speedBar.updateActualLife(trafficGame.numberOfCoinsForSuperSpeed.toFloat())

        labelScore.setText("Distance " + score + "m")
        labelCoin.setText(coins.toString() + "")
    }

    private fun setRunning() {
        stage.clear()
        state = STATE_RUNNING
        stage.addActor(tableScores)
        stage.addActor(speedBar)
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        setNewScore(score)
        Settings.coinsTotal += coins
        stage.clear()
        gameOverGroup = GameOverGroup(this, score, coins)
        stage.addActor(gameOverGroup)
        stage.addActor(labelTryAgain)
        stage.addActor(labelLeaderboard)
        stage.addActor(labelShopScreen)
        stage.addActor(buttonMusic)
    }

    private fun setReady() {
        state = STATE_READY

        val labelCounter = Label(TIME_TO_START.toString() + "", Assets.labelStyleLarge)
        labelCounter.setFontScale(2.5f)
        labelCounter.setPosition(SCREEN_WIDTH / 2f - labelCounter.width / 2f, 600f)
        labelCounter.setAlignment(Align.center)
        labelCounter.color.a = 0f
        labelCounter.addAction(Actions.repeat(3, Actions.sequence(Actions.fadeIn(1f), Actions.run {
            if (TIME_TO_START == 1) // Because the next time it is called it becomes 0.
                setRunning()
            TIME_TO_START--
            labelCounter.setText(TIME_TO_START.toString() + "")
            labelCounter.color.a = 0f
        })))

        if (Settings.numberOfTimesPlayed < 5) {
            stage.addActor(SwipeHorizontalTutorial())
        }

        stage.addActor(labelCounter)
    }

    private fun setPaused() {
        if (state == STATE_RUNNING || state == STATE_READY) {
            stage.clear()
            state = STATE_PAUSED
            stage.addActor(groupPaused)
        }
    }

    override fun pause() {
        setPaused()
        super.pause()
    }

    override fun hide() {
        super.hide()
        stageGame.dispose()
    }

    override fun draw(delta: Float) {
        stageGame.draw()
    }

    override fun left() {
        trafficGame.playerCar.tryMoveLeft()
        super.left()
    }

    override fun right() {
        trafficGame.playerCar.tryMoveRight()
        super.right()
    }

    override fun up() {
        if (!canSuperSpeed) return
        trafficGame.setSuperSpeed()
        canSuperSpeed = false
        super.up()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT, Input.Keys.A -> trafficGame.playerCar.tryMoveLeft()
            Input.Keys.RIGHT, Input.Keys.D -> trafficGame.playerCar.tryMoveRight()
            Input.Keys.ESCAPE, Input.Keys.BACK -> changeScreenWithFadeOut(
                MainMenuScreen::class.java, game
            )

            Input.Keys.SPACE -> trafficGame.setSuperSpeed()
        }

        return true
    }

    companion object {
        const val STATE_READY: Int = 1
        const val STATE_RUNNING: Int = 2
        const val STATE_PAUSED: Int = 3
        const val STATE_GAME_OVER: Int = 4
        var state: Int = 0
        var TIME_TO_START: Int = 3 // Time that appears at the beginning
    }
}
