package com.nopalsoft.clumsy.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.ClumsyUfoGame
import com.nopalsoft.clumsy.Settings
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade
import com.nopalsoft.clumsy.game.classic.ClassicGameScreen
import java.util.Random

abstract class Screens(game: ClumsyUfoGame) : InputAdapter(), Screen {
    var game: ClumsyUfoGame?

    var camera: OrthographicCamera?
    var batch: SpriteBatch
    var stage: Stage = game.stage!!

    var random: Random?
    var blackFadeOut: Image? = null

    init {
        this.stage.clear()
        this.batch = game.batch!!
        this.game = game

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera!!.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input

        random = Random()
        val ale = random!!.nextInt(3)

        when (ale) {
            0 -> Assets.background0 = Assets.background1
            1 -> Assets.background0 = Assets.background2
            else -> {
                Assets.background0 = Assets.background3
            }
        }
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)

        stage.act(delta)
        stage.draw()
    }

    fun changeScreenWithFadeOut(
        newScreen: Class<*>?,
        game: ClumsyUfoGame
    ) {
        blackFadeOut = Image(Assets.blackDrawable)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    when (newScreen) {
                        ClassicGameScreen::class.java -> game.setScreen(ClassicGameScreen(game))
                        MainMenuScreen::class.java -> game.setScreen(MainMenuScreen(game))
                        GameScreenArcade::class.java -> game.setScreen(GameScreenArcade(game))
                    }
                }
            )
        )
        stage.addActor(blackFadeOut)
    }

    fun drawScore(x: Float, y: Float, scoreNumericalValue: Int) {
        val score = scoreNumericalValue.toString()

        val len = score.length
        val charWidth = 42f
        val textWidth = len * charWidth
        for (i in 0..<len) {
            val keyFrame: AtlasRegion?

            val character = score[i]

            when (character) {
                '0' -> {
                    keyFrame = Assets.num0Large
                }

                '1' -> {
                    keyFrame = Assets.num1Large
                }

                '2' -> {
                    keyFrame = Assets.num2Large
                }

                '3' -> {
                    keyFrame = Assets.num3Large
                }

                '4' -> {
                    keyFrame = Assets.num4Large
                }

                '5' -> {
                    keyFrame = Assets.num5Large
                }

                '6' -> {
                    keyFrame = Assets.num6Large
                }

                '7' -> {
                    keyFrame = Assets.num7Large
                }

                '8' -> {
                    keyFrame = Assets.num8Large
                }

                else -> { // 9
                    keyFrame = Assets.num9Large
                }
            }

            batch.draw(
                keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                y, charWidth, 64f
            )
        }
    }

    fun drawScoreCentered(x: Float, y: Float, scoreNumericalValue: Int) {
        val score = scoreNumericalValue.toString()

        val len = score.length
        val charWidth = 42f
        var textWidth = 0f
        for (i in 0..<len) {
            val keyFrame: AtlasRegion?

            val character = score[i]

            when (character) {
                '0' -> {
                    keyFrame = Assets.num0Large
                }

                '1' -> {
                    keyFrame = Assets.num1Large
                }

                '2' -> {
                    keyFrame = Assets.num2Large
                }

                '3' -> {
                    keyFrame = Assets.num3Large
                }

                '4' -> {
                    keyFrame = Assets.num4Large
                }

                '5' -> {
                    keyFrame = Assets.num5Large
                }

                '6' -> {
                    keyFrame = Assets.num6Large
                }

                '7' -> {
                    keyFrame = Assets.num7Large
                }

                '8' -> {
                    keyFrame = Assets.num8Large
                }

                else -> {
                    keyFrame = Assets.num9Large
                }
            }

            batch.draw(keyFrame, x + textWidth, y, charWidth, 64f)
            textWidth += charWidth
        }
    }

    fun drawSmallScoreRightAligned(
        x: Float, y: Float,
        scoreNumericalValue: Int
    ) {
        val score = scoreNumericalValue.toString()

        val len = score.length
        var charWidth: Float
        var textWidth = 0f
        for (i in len - 1 downTo 0) {
            val keyFrame: AtlasRegion?

            charWidth = 22f
            val character = score[i]

            when (character) {
                '0' -> {
                    keyFrame = Assets.num0Small
                }

                '1' -> {
                    keyFrame = Assets.num1Small
                    charWidth = 11f
                }

                '2' -> {
                    keyFrame = Assets.num2Small
                }

                '3' -> {
                    keyFrame = Assets.num3Small
                }

                '4' -> {
                    keyFrame = Assets.num4Small
                }

                '5' -> {
                    keyFrame = Assets.num5Small
                }

                '6' -> {
                    keyFrame = Assets.num6Small
                }

                '7' -> {
                    keyFrame = Assets.num7Small
                }

                '8' -> {
                    keyFrame = Assets.num8Small
                }

                else -> {
                    keyFrame = Assets.num9Small
                }
            }
            textWidth += charWidth
            batch.draw(keyFrame, x - textWidth, y, charWidth, 32f)
        }
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        Settings.save()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800

        const val WORLD_SCREEN_WIDTH: Int = 4
        const val WORLD_SCREEN_HEIGHT: Int = 8
    }
}
