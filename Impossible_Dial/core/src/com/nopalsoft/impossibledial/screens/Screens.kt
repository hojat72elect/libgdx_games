package com.nopalsoft.impossibledial.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.impossibledial.Assets
import com.nopalsoft.impossibledial.MainGame
import com.nopalsoft.impossibledial.Settings.save
import com.nopalsoft.impossibledial.game.GameScreen

abstract class Screens(game: MainGame) : InputAdapter(), Screen {
    var game: MainGame?

    var oCam: OrthographicCamera
    var batcher: SpriteBatch?
    var stage = game.stage

    var blackFadeOut: Image? = null

    init {
        this.stage!!.clear()
        this.batcher = game.batcher
        this.game = game

        oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val input = InputMultiplexer(this, stage)
        Gdx.input.setInputProcessor(input)
    }

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        oCam.update()
        batcher!!.setProjectionMatrix(oCam.combined)
        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    @JvmOverloads
    fun changeScreenWithFadeOut(newScreen: Class<*>?, game: MainGame, level: Int = -1) {
        blackFadeOut = Image(Assets.pixelNegro)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) {
                game.setScreen(GameScreen(game, level))
            } else if (newScreen == MainMenuScreen::class.java) {
                game.setScreen(MainMenuScreen(game))
            }
        }))
        stage!!.addActor(blackFadeOut)
    }

    fun addEfectoPress(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.getX(), actor.getY() - 5)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.getX(), actor.getY() + 5)
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
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800
    }
}