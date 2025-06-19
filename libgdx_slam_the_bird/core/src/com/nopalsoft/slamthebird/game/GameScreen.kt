package com.nopalsoft.slamthebird.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Achievements.unlockCoins
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Assets.pauseMusic
import com.nopalsoft.slamthebird.Assets.playMusic
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.Settings.setBestScores
import com.nopalsoft.slamthebird.SlamTheBirdGame
import com.nopalsoft.slamthebird.scene2d.DialogPause
import com.nopalsoft.slamthebird.scene2d.DialogRate
import com.nopalsoft.slamthebird.scene2d.LabelCoins
import com.nopalsoft.slamthebird.scene2d.LabelCombo
import com.nopalsoft.slamthebird.scene2d.LabelScore
import com.nopalsoft.slamthebird.screens.BaseScreen
import com.nopalsoft.slamthebird.shop.ShopScreen

class GameScreen(game: SlamTheBirdGame) : BaseScreen(game) {
    private var worldGame: WorldGame
    private var renderer: WorldGameRender

    private var gameOverBackgroundImage: Image? = null

    private var groupTryAgain: Group
    private var groupButtons: Group? = null
    private var appTitleImage: Image? = null
    private var isComboTextOnLeft: Boolean = false // This variable makes the combo text appear left then right then left, right, left

    var dialogRate: DialogRate
    private var dialogPause: DialogPause

    private fun setUpButtons() {
        groupButtons = Group()
        groupButtons!!.setSize(stage!!.width, stage!!.height)
        groupButtons!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                if (dialogRate.isVisible) return false

                setRunning()
                Settings.playCount++
                return true
            }
        })

        val bestScore = Image(Assets.bestScore)
        bestScore.setSize(170f, 25f)
        bestScore
            .setPosition(SCREEN_WIDTH / 2f - bestScore.width / 2f, 770f)
        bestScore.addAction(
            Actions.repeat(
                Int.MAX_VALUE,
                Actions.sequence(
                    Actions.alpha(.6f, .75f),
                    Actions.alpha(1f, .75f)
                )
            )
        )

        val buttonShop = Button(Assets.buttonShop)
        buttonShop.setSize(90f, 70f)
        buttonShop.setPosition(0f, 730f)
        addPressEffect(buttonShop)
        buttonShop.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(ShopScreen::class.java, game)
            }
        })

        val buttonMore = Button(Assets.buttonMore)
        buttonMore.setSize(90f, 70f)
        buttonMore.setPosition(390f, 730f)
        addPressEffect(buttonMore)

        val buttonLeaderboard = Button(Assets.buttonLeaderboard)
        buttonLeaderboard.setSize(110f, 75f)
        buttonLeaderboard.setPosition((230 - 110).toFloat(), 310f)
        addPressEffect(buttonLeaderboard)

        val buttonAchievements = Button(Assets.buttonAchievements)
        buttonAchievements.setSize(110f, 75f)
        buttonAchievements.setPosition(250f, 310f)
        addPressEffect(buttonAchievements)

        val buttonRate = Button(Assets.buttonRate)
        buttonRate.setSize(110f, 75f)

        buttonRate.setPosition(SCREEN_WIDTH / 2f - buttonRate.width / 2f - 25, 220f) // Con el boton face y twitter cambia la pos
        addPressEffect(buttonRate)

        val btShareFacebook = Button(
            TextureRegionDrawable(
                Assets.buttonFacebook
            )
        )
        btShareFacebook.setSize(40f, 40f)
        btShareFacebook.setPosition(280f, 257f)
        addPressEffect(btShareFacebook)


        val btShareTwitter = Button(TextureRegionDrawable(Assets.buttonTwitter))
        btShareTwitter.setSize(40f, 40f)
        btShareTwitter.setPosition(280f, 212f)
        addPressEffect(btShareTwitter)

        val buttonMusic = Button(Assets.buttonStyleMusic)
        buttonMusic.setPosition(5f, 100f)
        buttonMusic.isChecked = !Settings.isMusicOn
        buttonMusic.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                event.stop()
                return true
            }
        })
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                event.stop()
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic.isChecked = !Settings.isMusicOn
                if (Settings.isMusicOn) playMusic()
                else pauseMusic()
                Gdx.app.log("Muscia", Settings.isMusicOn.toString() + "")
            }
        })

        val buttonSound = Button(Assets.buttonStyleSound)
        buttonSound.setPosition(5f, 180f)
        buttonSound.isChecked = !Settings.isSoundOn
        buttonSound.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                event.stop()
                return true
            }
        })
        buttonSound.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                buttonSound.isChecked = !Settings.isSoundOn
            }
        })

        val tapToPlay = Image(Assets.tapToPlay)
        tapToPlay.setSize(333f, 40f)
        tapToPlay
            .setPosition(SCREEN_WIDTH / 2f - tapToPlay.width / 2f, 140f)
        tapToPlay.setOrigin(
            tapToPlay.width / 2f,
            tapToPlay.height / 2f
        )
        val scaleTime = .75f
        tapToPlay.addAction(
            Actions.repeat(
                Int.MAX_VALUE, Actions.sequence(
                    Actions.scaleTo(.95f, .95f, scaleTime),
                    Actions.scaleTo(1f, 1f, scaleTime)
                )
            )
        )


        groupButtons!!.addActor(tapToPlay)
        groupButtons!!.addActor(bestScore)
        groupButtons!!.addActor(buttonShop)
        groupButtons!!.addActor(buttonMore)
        groupButtons!!.addActor(buttonLeaderboard)
        groupButtons!!.addActor(buttonAchievements)
        groupButtons!!.addActor(buttonRate)
        groupButtons!!.addActor(buttonMusic)
        groupButtons!!.addActor(buttonSound)
        groupButtons!!.addActor(btShareFacebook)
        groupButtons!!.addActor(btShareTwitter)
    }

    private fun setUpGameover() {
        gameOverBackgroundImage = Image(Assets.gameOverBackground)
        gameOverBackgroundImage!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        gameOverBackgroundImage!!.setOrigin(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f)
        gameOverBackgroundImage!!.setScale(2f)
        gameOverBackgroundImage!!.addAction(
            Actions.sequence(
                Actions.scaleTo(1.1f, 1.1f, .25f), Actions.delay(1f),
                Actions.run {
                    gameOverBackgroundImage!!.remove()
                    gameOverBackgroundImage!!.setScale(2f)
                    setTryAgain()
                })
        )
    }

    override fun update(delta: Float) {
        when (state) {
            STATE_RUNNING -> updateRunning(delta)
            STATE_READY, STATE_TRY_AGAIN -> updateReady(delta)
            else -> {}
        }
    }

    private fun updateReady(delta: Float) {
        var acelX = Gdx.input.accelerometerX * -1 / 5f

        if (Gdx.input.isKeyPressed(Input.Keys.A)) acelX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) acelX = 1f

        worldGame.updateReady(delta, acelX)
    }

    private var combo: Int = 0

    init {
        worldGame = WorldGame()
        renderer = WorldGameRender(batch!!, worldGame)

        groupTryAgain = Group()
        dialogRate = DialogRate(this)
        dialogPause = DialogPause(this)

        setUpButtons()
        setUpGameover()

        setReady()
    }

    private fun updateRunning(delta: Float) {
        var slam = false
        var acelX = Gdx.input.accelerometerX * -1 / 5f

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) acelX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        ) acelX = 1f
        Gdx.app.log("Slam is", " " + false)

        if (Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)
        ) {
            slam = true
            Gdx.app.log("Slam is", " " + true)
        }

        worldGame.update(delta, acelX, slam)

        if (worldGame.state == WorldGame.STATE_GAME_OVER) {
            setGameover()
        }

        if (worldGame.combo == 0) combo = 0

        if (worldGame.combo > combo) {
            stage!!.batch.setColor(1f, 1f, 1f, 1f) // Un BUG que no pone el alpha en 1 otra vez

            combo = worldGame.combo
            val lblCombo = LabelCombo(
                worldGame.player.position.x * 100,
                worldGame.player.position.y * 100 - 50, combo
            )

            val sideToMove: Float
            if (isComboTextOnLeft) {
                sideToMove = 0f
                isComboTextOnLeft = false
            } else {
                isComboTextOnLeft = true
                sideToMove = 380f
            }

            lblCombo.addAction(
                Actions.sequence(
                    Actions.moveTo(
                        sideToMove, 400f,
                        2.5f, Interpolation.exp10Out
                    ), Actions.removeActor()
                )
            )
            stage!!.addActor(lblCombo)
        }
    }

    override fun draw(delta: Float) {
        renderer.render()

        camera.update()
        batch!!.projectionMatrix = camera.combined

        batch!!.begin()

        when (state) {
            STATE_RUNNING -> drawRunning()
            STATE_READY, STATE_TRY_AGAIN -> drawReady()
            else -> {}
        }
        batch!!.end()
    }

    private fun drawRunning() {
        drawLargeNumberCenteredX(SCREEN_WIDTH / 2f, 700f, worldGame.scoreForSlammingEnemies)

        batch!!.draw(Assets.coinsRegion, 449f, 764f, 30f, 34f)
        drawSmallScoreRightAligned(445f, 764f, worldGame.coinsCollected)
    }

    private fun drawReady() {
        drawNumChicoCentradoX(SCREEN_WIDTH / 2f, 730f, Settings.bestScore)
    }

    private fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            dialogPause.show(stage!!)
        }
    }

    fun setRunningFromPaused() {
        if (state == STATE_PAUSED) {
            state = STATE_RUNNING
        }
    }

    private fun setReady() {
        appTitleImage = Image(Assets.title)
        appTitleImage!!.setSize(400f, 290f)
        appTitleImage!!.setPosition(
            SCREEN_WIDTH / 2f - appTitleImage!!.width / 2f,
            415f
        )
        state = STATE_READY
        stage!!.addActor(groupButtons)
        stage!!.addActor(appTitleImage)
    }

    private fun setRunning() {
        groupTryAgain.addAction(
            Actions.sequence(
                Actions.fadeOut(.5f),
                Actions.removeActor()
            )
        )
        appTitleImage!!.addAction(
            Actions.sequence(
                Actions.fadeOut(.5f),
                Actions.removeActor()
            )
        )
        groupButtons!!.addAction(
            Actions.sequence(
                Actions.fadeOut(.5f),
                Actions.run {
                    groupButtons!!.remove()
                    groupTryAgain.remove() // POr el bug
                    state = STATE_RUNNING
                })
        )
    }

    private fun setGameover() {
        setBestScores(worldGame.scoreForSlammingEnemies)

        state = STATE_GAME_OVER
        stage!!.addActor(gameOverBackgroundImage)
    }

    private fun setTryAgain() {
        state = STATE_TRY_AGAIN
        setUpGameover()

        groupTryAgain = Group()
        groupTryAgain.setSize(420f, 300f)
        groupTryAgain.setPosition(
            SCREEN_WIDTH / 2f - groupTryAgain.width
                    / 2, 800f
        )
        groupTryAgain.addAction(
            Actions.sequence(
                Actions.moveTo(
                    groupTryAgain.x, 410f, 1f, Interpolation.bounceOut
                ), Actions
                    .run {
                        groupButtons!!.addAction(Actions.fadeIn(.5f))
                        stage!!.addActor(groupButtons)
                        if (Settings.playCount % 7 == 0
                            && !Settings.didRateApp
                        ) {
                            dialogRate.show(stage!!)
                        }
                    })
        )

        val background = Image(Assets.scoresBackground)
        background.setSize(groupTryAgain.width, groupTryAgain.height)
        groupTryAgain.addActor(background)


        val score = Image(Assets.score)
        score.setSize(225f, 70f)
        score.setPosition(420 / 2f - score.width / 2f, 200f)

        val coinsEarned = Image(Assets.coinsEarned)
        coinsEarned.setSize(243f, 25f)
        coinsEarned.setPosition(25f, 47f)

        val labelScore = LabelScore(420 / 2f, 120f, worldGame.scoreForSlammingEnemies)
        val labelCoins = LabelCoins(385f, 45f, worldGame.coinsCollected)

        unlockCoins()

        groupTryAgain.addActor(score)
        groupTryAgain.addActor(labelScore)
        groupTryAgain.addActor(labelCoins)
        groupTryAgain.addActor(coinsEarned)

        worldGame = WorldGame()
        renderer = WorldGameRender(batch!!, worldGame)

        stage!!.addActor(groupTryAgain)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (state == STATE_READY) Gdx.app.exit()
            else if (state == STATE_TRY_AGAIN) changeScreenWithFadeOut(GameScreen::class.java, game)
            setPaused()
            return true
        }
        return false
    }

    companion object {
        const val STATE_READY: Int = 1
        const val STATE_RUNNING: Int = 2
        const val STATE_PAUSED: Int = 3
        const val STATE_GAME_OVER: Int = 4
        const val STATE_TRY_AGAIN: Int = 5

        var state: Int = 0
    }
}
