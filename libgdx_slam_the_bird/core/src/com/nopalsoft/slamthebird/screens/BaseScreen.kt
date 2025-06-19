package com.nopalsoft.slamthebird.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings.save
import com.nopalsoft.slamthebird.SlamTheBirdGame
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.shop.ShopScreen

abstract class BaseScreen(game: SlamTheBirdGame) : InputAdapter(), Screen {
    var game: SlamTheBirdGame

    var camera: OrthographicCamera
    var batch: SpriteBatch?
    var stage: Stage? = game.stage

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    fun drawLargeNumberCenteredX(x: Float, y: Float, score: Int) {
        val scoreText = score.toString()

        val length = scoreText.length
        val charWidth = 42f
        val textWidth = length * charWidth
        for (i in 0..<length) {
            val keyFrame: AtlasRegion?

            val character = scoreText[i]

            keyFrame = when (character) {
                '0' -> {
                    Assets.largeNum0
                }

                '1' -> {
                    Assets.largeNum1
                }

                '2' -> {
                    Assets.largeNum2
                }

                '3' -> {
                    Assets.largeNum3
                }

                '4' -> {
                    Assets.largeNum4
                }

                '5' -> {
                    Assets.largeNum5
                }

                '6' -> {
                    Assets.largeNum6
                }

                '7' -> {
                    Assets.largeNum7
                }

                '8' -> {
                    Assets.largeNum8
                }

                else -> {
                    Assets.largeNum9
                }
            }

            batch!!.draw(
                keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                y, charWidth, 64f
            )
        }
    }

    fun drawSmallScoreRightAligned(
        x: Float, y: Float,
        score: Int
    ) {
        val scoreText = score.toString()

        val len = scoreText.length
        var charWidth: Float
        var textWidth = 0f
        for (i in len - 1 downTo 0) {
            val keyFrame: AtlasRegion?

            charWidth = 22f
            val character = scoreText[i]

            when (character) {
                '0' -> {
                    keyFrame = Assets.smallNum0
                }

                '1' -> {
                    keyFrame = Assets.smallNum1
                    charWidth = 11f
                }

                '2' -> {
                    keyFrame = Assets.smallNum2
                }

                '3' -> {
                    keyFrame = Assets.smallNum3
                }

                '4' -> {
                    keyFrame = Assets.smallNum4
                }

                '5' -> {
                    keyFrame = Assets.smallNum5
                }

                '6' -> {
                    keyFrame = Assets.smallNum6
                }

                '7' -> {
                    keyFrame = Assets.smallNum7
                }

                '8' -> {
                    keyFrame = Assets.smallNum8
                }

                else -> {
                    keyFrame = Assets.smallNum9
                }
            }
            textWidth += charWidth
            batch!!.draw(keyFrame, x - textWidth, y, charWidth, 32f)
        }
    }

    fun drawNumChicoCentradoX(x: Float, y: Float, puntuacion: Int) {
        val score = puntuacion.toString()

        val len = score.length
        val charWidth = 22f
        val textWidth = len * charWidth
        for (i in 0..<len) {
            val keyFrame: AtlasRegion?

            val character = score[i]

            keyFrame = when (character) {
                '0' -> {
                    Assets.smallNum0
                }

                '1' -> {
                    Assets.smallNum1
                }

                '2' -> {
                    Assets.smallNum2
                }

                '3' -> {
                    Assets.smallNum3
                }

                '4' -> {
                    Assets.smallNum4
                }

                '5' -> {
                    Assets.smallNum5
                }

                '6' -> {
                    Assets.smallNum6
                }

                '7' -> {
                    Assets.smallNum7
                }

                '8' -> {
                    Assets.smallNum8
                }

                else -> {
                    Assets.smallNum9
                }
            }

            batch!!.draw(
                keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                y, charWidth, 32f
            )
        }
    }

    private var blackFadeOut: Image? = null

    init {
        stage!!.clear()
        this.batch = game.batch
        this.game = game

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f

        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input
    }

    fun changeScreenWithFadeOut(
        newScreen: Class<*>,
        game: SlamTheBirdGame
    ) {
        blackFadeOut = Image(Assets.blackPixel)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.color.a = 0f
        blackFadeOut!!.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    if (newScreen == GameScreen::class.java) game.screen = GameScreen(game)
                    else if (newScreen == ShopScreen::class.java) game.screen = ShopScreen(game)
                })
        )
        stage!!.addActor(blackFadeOut)
    }

    fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 5)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.x, actor.y + 5)
            }
        })
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        save()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        stage!!.dispose()
        batch!!.dispose()
        save()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800

        const val WORLD_SCREEN_WIDTH: Float = 4.8f
        const val WORLD_SCREEN_HEIGHT: Float = 8f
    }
}
