package com.nopalsoft.clumsy.game.arcade

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
import com.nopalsoft.clumsy.game.classic.ClassicGameScreen
import com.nopalsoft.clumsy.objects.Ufo
import com.nopalsoft.clumsy.screens.MainMenuScreen
import com.nopalsoft.clumsy.screens.Screens

class GameScreenArcade(game: ClumsyUfoGame) : Screens(game) {
    val TIME_INC_GAMEOVER: Float = .0025f
    var numIncGameOver: Int = 0
    var comenzarIncrementarPuntuacionGameOver: Boolean
    var oWorld: WorldGameArcade
    var renderer: WorldGameRendererArcade
    var salto: Boolean = false
    var flashazo: Image

    /* Game Over */
    var medallsFondo: Group? = null
    var gameOver: Image? = null

    /* Ready */
    var getReady: Image? = null
    var tapCat: Image? = null
    var btPlayClassic: Button? = null
    var btPlayArcade: Button? = null
    var btScore: Button? = null
    var btRate: Button? = null
    var btRestorePurchases: Button? = null
    var btNoAds: Button? = null
    var bottomMenu: Table? = null // rate,ads,restore purchses
    var btShareFacebook: Button? = null
    var btShareTwitter: Button? = null
    var timeIncGameOver: Float

    init {
        Settings.numberOfTimesPlayed++
        oWorld = WorldGameArcade()
        renderer = WorldGameRendererArcade(batch, oWorld)
        state = STATE_READY
        comenzarIncrementarPuntuacionGameOver = false
        timeIncGameOver = 0f

        flashazo = Image(Assets.whiteDrawable)
        flashazo.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        flashazo.addAction(
            Actions.sequence(
                Actions.fadeOut(Ufo.HURT_DURATION),
                Actions.run(object : Runnable {
                    override fun run() {
                        flashazo.remove()
                    }
                })
            )
        )

        inicializarGameOver()
        inicializarReady()
    }

    private fun inicializarReady() {
        getReady = Image(Assets.getReady)
        getReady!!.setSize(320f, 100f)
        getReady!!.setPosition(SCREEN_WIDTH / 2f - 160, SCREEN_HEIGHT / 2f + 50)
        getReady!!.getColor().a = 0f
        getReady!!.addAction(Actions.fadeIn(.4f))

        tapCat = Image(Assets.tapCat)
        tapCat!!.setSize(150f, 140f)
        tapCat!!.setPosition(SCREEN_WIDTH / 2f - 75, SCREEN_HEIGHT / 2f - 100)
        tapCat!!.getColor().a = 0f
        tapCat!!.addAction(Actions.fadeIn(.4f))

        stage.addActor(getReady)
        stage.addActor(tapCat)
    }

    private fun inicializarGameOver() {
        medallsFondo = Group()
        medallsFondo!!.setSize(400f, 200f)
        val background = Image(Assets.medalsBackground)
        background.setSize(medallsFondo!!.getWidth(), medallsFondo!!.getHeight())
        medallsFondo!!.setPosition(SCREEN_WIDTH / 2f - 200, -201f)
        medallsFondo!!.addActor(background)

        val action = Actions.action<MoveToAction>(MoveToAction::class.java)
        action.interpolation = Interpolation.sine
        action.setPosition(SCREEN_WIDTH / 2f - 200, 385f)
        action.duration = .25f
        medallsFondo!!.addAction(
            Actions.sequence(
                action, Actions.delay(.1f),
                Actions.run(object : Runnable {
                    override fun run() {
                        comenzarIncrementarPuntuacionGameOver = true
                        if (numIncGameOver.toFloat() == oWorld.score) {
                            stage.addActor(btPlayClassic)
                            stage.addActor(btPlayArcade)
                            stage.addActor(btScore)
                            stage.addActor(btShareFacebook)
                            stage.addActor(btShareTwitter)
                            stage.addActor(bottomMenu)
                        }
                    }
                })
            )
        )

        btPlayClassic = Button(
            TextureRegionDrawable(
                Assets.buttonPlayClassic
            )
        )
        btPlayClassic!!.setSize(160f, 95f)
        btPlayClassic!!.setPosition(75f, 280f)
        btPlayClassic!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btPlayClassic!!.setPosition(75f, 277f)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btPlayClassic!!.setPosition(75f, 280f)
                fadeOutButtons()
                state = STATE_TRY_AGAIN
                Assets.playSound(Assets.swooshing)
                changeScreenWithFadeOut(ClassicGameScreen::class.java, game)
            }
        })

        btPlayArcade = Button(
            TextureRegionDrawable(Assets.buttonPlayArcade)
        )
        btPlayArcade!!.setSize(160f, 95f)
        btPlayArcade!!.setPosition(250f, 280f)
        btPlayArcade!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btPlayArcade!!.setPosition(250f, 277f)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btPlayArcade!!.setPosition(250f, 280f)
                fadeOutButtons()
                state = STATE_TRY_AGAIN
                Assets.playSound(Assets.swooshing)
                changeScreenWithFadeOut(GameScreenArcade::class.java, game)
            }
        })

        btScore = Button(TextureRegionDrawable(Assets.buttonLeaderboard))
        btScore!!.setSize(160f, 95f)
        btScore!!.setPosition(130f, 180f)
        btScore!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btScore!!.setPosition(btScore!!.getX(), btScore!!.getY() - 3)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btScore!!.setPosition(btScore!!.getX(), btScore!!.getY() + 3)
            }
        })

        btShareFacebook = Button(
            TextureRegionDrawable(
                Assets.buttonFacebook
            )
        )
        btShareFacebook!!.setSize(45f, 45f)
        btShareFacebook!!.setPosition(295f, 230f)
        btShareFacebook!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btShareFacebook!!.setPosition(295f, 227f)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btShareFacebook!!.setPosition(295f, 230f)
            }
        })

        btShareTwitter = Button(TextureRegionDrawable(Assets.buttonTwitter))
        btShareTwitter!!.setSize(45f, 45f)
        btShareTwitter!!.setPosition(295f, 181f)
        btShareTwitter!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btShareTwitter!!.setPosition(
                    btShareTwitter!!.getX(),
                    btShareTwitter!!.getY() - 3
                )
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btShareTwitter!!.setPosition(
                    btShareTwitter!!.getX(),
                    btShareTwitter!!.getY() + 3
                )
            }
        })

        btRate = Button(TextureRegionDrawable(Assets.buttonRate))
        btRate!!.setSize(60f, 60f)
        btRate!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btRate!!.setPosition(btRate!!.getX(), btRate!!.getY() - 3)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btRate!!.setPosition(btRate!!.getX(), btRate!!.getY() + 3)
            }
        })

        btNoAds = Button(TextureRegionDrawable(Assets.buttonNoAds))
        if (Settings.didBuyNoAds) btNoAds!!.isVisible = false
        btNoAds!!.setSize(60f, 60f)
        btNoAds!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btNoAds!!.setPosition(btNoAds!!.getX(), btNoAds!!.getY() - 3)
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btNoAds!!.setPosition(btNoAds!!.getX(), btNoAds!!.getY() + 3)
            }
        })

        btRestorePurchases = Button(TextureRegionDrawable(Assets.buttonRestorePurchases))
        btRestorePurchases!!.setSize(60f, 60f)
        btRestorePurchases!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                btRestorePurchases!!.setPosition(
                    btRestorePurchases!!.getX(),
                    btRestorePurchases!!.getY() - 3
                )
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                btRestorePurchases!!.setPosition(
                    btRestorePurchases!!.getX(),
                    btRestorePurchases!!.getY() + 3
                )
            }
        })

        bottomMenu = Table()
        bottomMenu!!.setPosition(1f, 1f)
        bottomMenu!!.defaults().padRight(2.5f)

        bottomMenu!!.add<Button?>(btRate)
        bottomMenu!!.add<Button?>(btRestorePurchases)
        bottomMenu!!.add<Button?>(btNoAds)
        bottomMenu!!.pack()

        gameOver = Image(Assets.gameover)
        gameOver!!.setSize(320f, 100f)
        gameOver!!.setPosition(SCREEN_WIDTH / 2f - 160, 600f)
    }

    private fun fadeOutButtons() {
        medallsFondo!!.addAction(Actions.fadeOut(.2f))
        btPlayClassic!!.addAction(Actions.fadeOut(.2f))
        btPlayArcade!!.addAction(Actions.fadeOut(.2f))
        btScore!!.addAction(Actions.fadeOut(.2f))
        gameOver!!.addAction(Actions.fadeOut(.2f))
        btShareFacebook!!.addAction(Actions.fadeOut(.2f))
        btShareTwitter!!.addAction(Actions.fadeOut(.2f))
        bottomMenu!!.addAction(Actions.fadeOut(.2f))
    }

    override fun update(delta: Float) {
        if (Settings.didBuyNoAds) btNoAds!!.isVisible = false

        when (state) {
            STATE_READY -> updateReady()
            STATE_RUNNING -> updateRunning(delta)
            STATE_GAME_OVER -> updateGameOver(delta)
            else -> {}
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched()) {
            getReady!!.remove()
            tapCat!!.remove()
            state = STATE_RUNNING
        }
    }

    private fun updateGameOver(delta: Float) {
        timeIncGameOver += delta
        if (comenzarIncrementarPuntuacionGameOver
            && numIncGameOver < oWorld.score.toInt() && timeIncGameOver >= TIME_INC_GAMEOVER
        ) {
            timeIncGameOver -= TIME_INC_GAMEOVER
            numIncGameOver++

            if (numIncGameOver == oWorld.score.toInt()) {
                stage.addActor(btPlayClassic)
                stage.addActor(btScore)
                stage.addActor(btPlayArcade)
                stage.addActor(btShareFacebook)
                stage.addActor(btShareTwitter)
                stage.addActor(bottomMenu)

                if (oWorld.score >= 50) {
                    val med: AtlasRegion?

                    if (oWorld.score >= 250) med = Assets.med1
                    else if (oWorld.score >= 200) med = Assets.med2
                    else if (oWorld.score >= 100) med = Assets.med3
                    else med = Assets.med4

                    val medalla = Image(med)
                    medalla.setSize(90f, 90f)
                    medalla.setPosition(45f, 47f)
                    medallsFondo!!.addActor(medalla)
                }
            }
        }
    }

    private fun updateRunning(delta: Float) {
        if (Gdx.input.justTouched()) salto = true

        oWorld.update(delta, salto)

        if (oWorld.oUfo.state == Ufo.STATE_HURT) {
            stage.addActor(flashazo)
            oWorld.oUfo.die()
        }

        if (oWorld.state == WorldGameArcade.STATE_GAMEOVER) {
            setGameover()
        }

        salto = false
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        if (Settings.bestScoreArcade < oWorld.score) Settings.bestScoreArcade = oWorld.score.toInt()
        stage.addActor(medallsFondo)
        stage.addActor(gameOver)
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

        if (state != STATE_GAME_OVER) drawScoreCentered(
            0f, (SCREEN_HEIGHT - 65).toFloat(),
            oWorld.score.toInt()
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
            medallsFondo!!.getX() + medallsFondo!!.getWidth() - 30,
            medallsFondo!!.getY() + 40, Settings.bestScoreArcade
        )
        drawSmallScoreRightAligned(
            medallsFondo!!.getX() + medallsFondo!!.getWidth() - 30,
            medallsFondo!!.getY() + 110, numIncGameOver
        )
    }

    private fun drawReady(delta: Float) {
        oWorld.oUfo.update(delta, null)
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
