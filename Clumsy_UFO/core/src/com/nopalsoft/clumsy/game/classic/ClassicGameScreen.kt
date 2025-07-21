package com.nopalsoft.clumsy.game.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.ClumsyUfoGame
import com.nopalsoft.clumsy.Settings
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade
import com.nopalsoft.clumsy.objects.Ufo
import com.nopalsoft.clumsy.screens.MainMenuScreen
import com.nopalsoft.clumsy.screens.Screens

class ClassicGameScreen(game: ClumsyUfoGame) : Screens(game) {
    val TIME_INC_GAMEOVER: Float = .035f
    var numIncGameOver: Int = 0
    var comenzarIncrementarPuntuacionGameOver: Boolean
    var oWorld: WorldGameClassic
    var renderer: WorldGameRenderer
    var salto: Boolean = false
    var hurtFlashImage: Image

    var GameOverBackground: Group? = null
    var gameOverImage: Image? = null

    var getReadyImage: Image? = null
    var tapCatImage: Image? = null
    var buttonPlayClassic: Button? = null
    var buttonPlayArcade: Button? = null
    var buttonScore: Button? = null
    var buttonRate: Button? = null
    var buttonRestorePurchases: Button? = null
    var buttonNoAds: Button? = null
    var bottomMenu: Table? = null
    var buttonShareFacebook: Button? = null
    var buttonShareTwitter: Button? = null
    var timeIncGameOver: Float

    init {
        Settings.numberOfTimesPlayed++
        oWorld = WorldGameClassic()
        renderer = WorldGameRenderer(batch, oWorld)
        state = STATE_READY
        comenzarIncrementarPuntuacionGameOver = false
        timeIncGameOver = 0f

        hurtFlashImage = Image(Assets.whiteDrawable)
        hurtFlashImage.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        hurtFlashImage.addAction(
            Actions.sequence(
                Actions.fadeOut(Ufo.HURT_DURATION),
                Actions.run(object : Runnable {
                    override fun run() {
                        hurtFlashImage.remove()
                    }
                })
            )
        )

        initializeGameOverScreen()
        setupReadyScreen()
    }

    private fun setupReadyScreen() {
        getReadyImage = Image(Assets.getReady)
        getReadyImage!!.setSize(320f, 100f)
        getReadyImage!!.setPosition(SCREEN_WIDTH / 2f - 160, SCREEN_HEIGHT / 2f + 50)
        getReadyImage!!.getColor().a = 0f
        getReadyImage!!.addAction(Actions.fadeIn(.4f))

        tapCatImage = Image(Assets.tapCat)
        tapCatImage!!.setSize(150f, 140f)
        tapCatImage!!.setPosition(SCREEN_WIDTH / 2f - 75, SCREEN_HEIGHT / 2f - 100)
        tapCatImage!!.getColor().a = 0f
        tapCatImage!!.addAction(Actions.fadeIn(.4f))

        stage.addActor(getReadyImage)
        stage.addActor(tapCatImage)
    }

    private fun initializeGameOverScreen() {
        GameOverBackground = Group()
        GameOverBackground!!.setSize(400f, 200f)
        val background = Image(Assets.medalsBackground)
        background.setSize(GameOverBackground!!.getWidth(), GameOverBackground!!.getHeight())
        GameOverBackground!!.setPosition(SCREEN_WIDTH / 2f - 200, -201f)
        GameOverBackground!!.addActor(background)

        val action = Actions.action<MoveToAction>(MoveToAction::class.java)
        action.interpolation = Interpolation.sine
        action.setPosition(SCREEN_WIDTH / 2f - 200, 385f)
        action.duration = .25f
        GameOverBackground!!.addAction(
            Actions.sequence(
                action, Actions.delay(.1f),
                Actions.run(object : Runnable {
                    override fun run() {
                        comenzarIncrementarPuntuacionGameOver = true
                        if (numIncGameOver == oWorld.score) {
                            stage.addActor(buttonPlayClassic)
                            stage.addActor(buttonPlayArcade)
                            stage.addActor(buttonScore)
                            stage.addActor(buttonShareFacebook)
                            stage.addActor(buttonShareTwitter)
                            stage.addActor(bottomMenu)
                        }
                    }
                })
            )
        )

        buttonPlayClassic = Button(
            TextureRegionDrawable(
                Assets.buttonPlayClassic
            )
        )
        buttonPlayClassic!!.setSize(160f, 95f)
        buttonPlayClassic!!.setPosition(75f, 280f)
        buttonPlayClassic!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonPlayClassic!!.setPosition(75f, 277f)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonPlayClassic!!.setPosition(75f, 280f)
                fadeOutButtons()
                state = STATE_TRY_AGAIN
                Assets.playSound(Assets.swooshing)
                changeScreenWithFadeOut(ClassicGameScreen::class.java, game)
            }
        })

        buttonPlayArcade = Button(
            TextureRegionDrawable(Assets.buttonPlayArcade)
        )
        buttonPlayArcade!!.setSize(160f, 95f)
        buttonPlayArcade!!.setPosition(250f, 280f)
        buttonPlayArcade!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonPlayArcade!!.setPosition(250f, 277f)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonPlayArcade!!.setPosition(250f, 280f)
                fadeOutButtons()
                state = STATE_TRY_AGAIN
                Assets.playSound(Assets.swooshing)
                changeScreenWithFadeOut(GameScreenArcade::class.java, game)
            }
        })

        buttonScore = Button(TextureRegionDrawable(Assets.buttonLeaderboard))
        buttonScore!!.setSize(160f, 95f)
        buttonScore!!.setPosition(130f, 180f)
        buttonScore!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonScore!!.setPosition(buttonScore!!.getX(), buttonScore!!.getY() - 3)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonScore!!.setPosition(buttonScore!!.getX(), buttonScore!!.getY() + 3)
            }
        })

        buttonShareFacebook = Button(
            TextureRegionDrawable(
                Assets.buttonFacebook
            )
        )
        buttonShareFacebook!!.setSize(45f, 45f)
        buttonShareFacebook!!.setPosition(295f, 230f)
        buttonShareFacebook!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonShareFacebook!!.setPosition(295f, 227f)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonShareFacebook!!.setPosition(295f, 230f)
            }
        })

        buttonShareTwitter = Button(TextureRegionDrawable(Assets.buttonTwitter))
        buttonShareTwitter!!.setSize(45f, 45f)
        buttonShareTwitter!!.setPosition(295f, 181f)
        buttonShareTwitter!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonShareTwitter!!.setPosition(
                    buttonShareTwitter!!.getX(),
                    buttonShareTwitter!!.getY() - 3
                )
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonShareTwitter!!.setPosition(
                    buttonShareTwitter!!.getX(),
                    buttonShareTwitter!!.getY() + 3
                )
            }
        })

        buttonRate = Button(TextureRegionDrawable(Assets.buttonRate))
        buttonRate!!.setSize(60f, 60f)
        buttonRate!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonRate!!.setPosition(buttonRate!!.getX(), buttonRate!!.getY() - 3)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonRate!!.setPosition(buttonRate!!.getX(), buttonRate!!.getY() + 3)
            }
        })

        buttonNoAds = Button(TextureRegionDrawable(Assets.buttonNoAds))
        if (Settings.didBuyNoAds) buttonNoAds!!.isVisible = false
        buttonNoAds!!.setSize(60f, 60f)
        buttonNoAds!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonNoAds!!.setPosition(buttonNoAds!!.getX(), buttonNoAds!!.getY() - 3)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonNoAds!!.setPosition(buttonNoAds!!.getX(), buttonNoAds!!.getY() + 3)
            }
        })

        buttonRestorePurchases = Button(TextureRegionDrawable(Assets.buttonRestorePurchases))
        buttonRestorePurchases!!.setSize(60f, 60f)
        buttonRestorePurchases!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                buttonRestorePurchases!!.setPosition(
                    buttonRestorePurchases!!.getX(),
                    buttonRestorePurchases!!.getY() - 3
                )
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                buttonRestorePurchases!!.setPosition(
                    buttonRestorePurchases!!.getX(),
                    buttonRestorePurchases!!.getY() + 3
                )
            }
        })

        bottomMenu = Table()
        bottomMenu!!.setPosition(1f, 1f)
        bottomMenu!!.defaults().padRight(2.5f)

        bottomMenu!!.add<Button?>(buttonRate)
        bottomMenu!!.add<Button?>(buttonRestorePurchases)
        bottomMenu!!.add<Button?>(buttonNoAds)
        bottomMenu!!.pack()

        gameOverImage = Image(Assets.gameover)
        gameOverImage!!.setSize(320f, 100f)
        gameOverImage!!.setPosition(SCREEN_WIDTH / 2f - 160, 600f)
    }

    private fun fadeOutButtons() {
        GameOverBackground!!.addAction(Actions.fadeOut(.2f))
        buttonPlayClassic!!.addAction(Actions.fadeOut(.2f))
        buttonPlayArcade!!.addAction(Actions.fadeOut(.2f))
        buttonScore!!.addAction(Actions.fadeOut(.2f))
        gameOverImage!!.addAction(Actions.fadeOut(.2f))
        buttonShareFacebook!!.addAction(Actions.fadeOut(.2f))
        buttonShareTwitter!!.addAction(Actions.fadeOut(.2f))
        bottomMenu!!.addAction(Actions.fadeOut(.2f))
    }

    override fun update(delta: Float) {
        if (Settings.didBuyNoAds) buttonNoAds!!.isVisible = false

        when (state) {
            STATE_READY -> updateReady()
            STATE_RUNNING -> updateRunning(delta)
            STATE_GAME_OVER -> updateGameOver(delta)
            else -> {}
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched()) {
            getReadyImage!!.remove()
            tapCatImage!!.remove()
            state = STATE_RUNNING
        }
    }

    private fun updateGameOver(delta: Float) {
        timeIncGameOver += delta
        if (comenzarIncrementarPuntuacionGameOver
            && numIncGameOver < oWorld.score && timeIncGameOver >= TIME_INC_GAMEOVER
        ) {
            timeIncGameOver -= TIME_INC_GAMEOVER
            numIncGameOver++

            if (numIncGameOver == oWorld.score) {
                stage.addActor(buttonPlayClassic)
                stage.addActor(buttonScore)
                stage.addActor(buttonPlayArcade)
                stage.addActor(buttonShareFacebook)
                stage.addActor(buttonShareTwitter)
                stage.addActor(bottomMenu)

                if (oWorld.score >= 10) {
                    val med: AtlasRegion?
                    if (oWorld.score >= 40) med = Assets.med1
                    else if (oWorld.score >= 30) med = Assets.med2
                    else if (oWorld.score >= 20) med = Assets.med3
                    else med = Assets.med4

                    val medalla = Image(med)
                    medalla.setSize(90f, 90f)
                    medalla.setPosition(45f, 47f)
                    GameOverBackground!!.addActor(medalla)
                }
            }
        }
    }

    private fun updateRunning(delta: Float) {
        if (Gdx.input.justTouched()) salto = true

        oWorld.update(delta, salto)

        if (oWorld.oUfo!!.state == Ufo.STATE_HURT) {
            stage.addActor(hurtFlashImage)
            oWorld.oUfo!!.die()
        }

        if (oWorld.state == WorldGameClassic.STATE_GAMEOVER) {
            setGameover()
        }

        salto = false
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        if (Settings.bestScoreClassic < oWorld.score) Settings.bestScoreClassic = oWorld.score
        stage.addActor(GameOverBackground)
        stage.addActor(gameOverImage)
    }

    override fun draw(delta: Float) {
        var delta = delta
        if (state == STATE_PAUSED || state == STATE_GAME_OVER) delta = 0f

        batch.begin()
        batch.draw(Assets.background0, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch.end()

        renderer.render()
        Assets.parallaxBackground.render(delta)

        camera.update()
        batch.setProjectionMatrix(camera.combined)
        batch.begin()

        if (state == STATE_READY) drawReady(delta)

        if (state != STATE_GAME_OVER) drawScore(
            SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f + 250,
            oWorld.score
        )
        batch.end()
    }

    override fun render(delta: Float) {
        super.render(delta)

        if (state == STATE_GAME_OVER) {
            batch.begin()
            drawGameover()
            batch.end()
        }
    }

    private fun drawGameover() {
        drawSmallScoreRightAligned(
            GameOverBackground!!.getX() + GameOverBackground!!.getWidth() - 30,
            GameOverBackground!!.getY() + 40, Settings.bestScoreClassic
        )
        drawSmallScoreRightAligned(
            GameOverBackground!!.getX() + GameOverBackground!!.getWidth() - 30,
            GameOverBackground!!.getY() + 110, numIncGameOver
        )
    }

    private fun drawReady(delta: Float) {
        oWorld.oUfo!!.update(delta, null)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE) {
            salto = true
            return true
        } else if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) game.setScreen(MainMenuScreen(game))
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
