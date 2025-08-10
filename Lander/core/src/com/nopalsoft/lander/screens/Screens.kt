package com.nopalsoft.lander.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.nopalsoft.lander.MainLander
import com.nopalsoft.lander.Settings

abstract class Screens(open val game: MainLander) : InputAdapter(), Screen {

    @JvmField
    var oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())

    @JvmField
    var batcher = game.batcher

    @JvmField
    var stage = game.stage

    protected var screenStateTime = 0F
    protected var screenLastStateTime = 0F

    init {
        this.stage.clear()
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)
        Gdx.input.inputProcessor = InputMultiplexer(stage, this)
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        screenLastStateTime = screenStateTime
        screenStateTime += delta
        update(delta)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)

        stage.act(delta)
        stage.draw()
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(SCREEN_WIDTH, SCREEN_HEIGHT, true)
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
        batcher.dispose()
    }

    companion object {
        const val SCREEN_WIDTH = 480
        const val SCREEN_HEIGHT = 800
        const val WORLD_SCREEN_WIDTH = 4.8f
        const val WORLD_SCREEN_HEIGHT = 8f
    }
}
