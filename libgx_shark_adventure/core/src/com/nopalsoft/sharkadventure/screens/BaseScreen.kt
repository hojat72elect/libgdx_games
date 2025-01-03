package com.nopalsoft.sharkadventure.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.nopalsoft.sharkadventure.MainShark
import com.nopalsoft.sharkadventure.Settings.save

/**
 * The base logic shared by all screens in a game.
 */
abstract class BaseScreen(var game: MainShark) : InputAdapter(), Screen {
    var camera: OrthographicCamera
    var batcher: SpriteBatch
    var stage = game.stage

    init {
        stage.clear()
        batcher = game.batcher

        camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)
        camera.position.set(SCREEN_WIDTH / 2F, SCREEN_HEIGHT / 2F, 0F)

        val input = InputMultiplexer(stage, this)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        update(delta)
        stage.act(delta)

        camera.update()
        batcher.projectionMatrix = camera.combined
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)
        stage.draw()
    }

    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        save()
    }

    override fun pause() {
        save()
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    companion object {
        const val SCREEN_WIDTH = 800F
        const val SCREEN_HEIGHT = 480F
        const val WORLD_WIDTH = 8F
        const val WORLD_HEIGHT = 4.8F
    }
}
