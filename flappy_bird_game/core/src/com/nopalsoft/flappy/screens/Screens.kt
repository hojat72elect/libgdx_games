package com.nopalsoft.flappy.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.flappy.MainFlappyBird

abstract class Screens(val game: MainFlappyBird) : InputAdapter(), Screen {

    val camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)
    val spriteBatch = SpriteBatch()
    val stage = Stage(StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));

    init {
        // Center the camera on the screen
        camera.position.set(SCREEN_WIDTH / 2F, SCREEN_HEIGHT / 2F, 0F)

        // Is needed to tell the InputAdapter and stage when we receive events
        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input

    }

    // Will be called 60 times per second (60 FPS)
    override fun render(delta: Float) {

        // Update all the physics of the game
        update(delta)

        // Update the stage (mostly UI elements)
        stage.act(delta)

        // Clear everything on the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Draw the game elements on the screen
        draw(delta)

        // Draw the stage element on the screen
        stage.draw()
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    companion object {
        const val SCREEN_WIDTH = 480F
        const val SCREEN_HEIGHT = 800F
        const val WORLD_WIDTH = 4.8F
        const val WORLD_HEIGHT = 8F
    }
}