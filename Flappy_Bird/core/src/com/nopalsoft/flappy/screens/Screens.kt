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


abstract class Screens(var game: MainFlappyBird) : InputAdapter(), Screen {

    // Create the UI Camera and center it on the screen
    var camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)
    var spriteBatch = SpriteBatch()

    // We will add UI elements to the stage
    var stage = Stage(StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT))

    init {
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)
        // We need it to tell the InputAdapter and stage when we receive events
        Gdx.input.inputProcessor = InputMultiplexer(this, stage)
    }

    // This functions will be called automatically 60 times per second (60 FPS)
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

    override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, true)


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
        const val SCREEN_WIDTH = 480f
        const val SCREEN_HEIGHT = 800f
        const val WORLD_WIDTH = 4.8f
        const val WORLD_HEIGHT = 8f
    }
}
