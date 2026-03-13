package com.nopalsoft.thetruecolor.screens

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
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Settings.save
import com.nopalsoft.thetruecolor.TrueColorGame
import com.nopalsoft.thetruecolor.game.GameScreen

abstract class BaseScreen(game: TrueColorGame) : InputAdapter(), Screen {

    var game: TrueColorGame?
    var camera: OrthographicCamera
    var batch: SpriteBatch?
    var stage = game.stage

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batch!!.setProjectionMatrix(camera.combined)
        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    var blackFadeOut: Image? = null

    init {
        this.stage!!.clear()
        this.batch = game.batch
        this.game = game

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val input = InputMultiplexer(this, stage)
        Gdx.input.setInputProcessor(input)
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>?, game: TrueColorGame) {
        blackFadeOut = Image(Assets.blackPixelDrawable)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) game.setScreen(GameScreen(game))
            else if (newScreen == MainMenuScreen::class.java) game.setScreen(MainMenuScreen(game))
        }))
        stage!!.addActor(blackFadeOut)
    }

    fun addPressEffect(actor: Actor) {
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

    override fun hide() {
        save()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800
    }
}