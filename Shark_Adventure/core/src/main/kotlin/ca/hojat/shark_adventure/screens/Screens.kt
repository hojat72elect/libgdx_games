package ca.hojat.shark_adventure.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import ca.hojat.shark_adventure.Settings.save
import ca.hojat.shark_adventure.SharkAdventureGame

abstract class Screens(var game: SharkAdventureGame) : InputAdapter(), Screen {
    var camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
    var batch = game.batch
    var stage = game.stage

    init {
        stage!!.clear()
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val input = InputMultiplexer(stage, this)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        update(delta)
        stage!!.act(delta)

        camera.update()
        batch!!.setProjectionMatrix(camera.combined)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)
        stage!!.draw()
    }

    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
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
        const val SCREEN_WIDTH = 800
        const val SCREEN_HEIGHT = 480

        const val WORLD_WIDTH = 8f
        const val WORLD_HEIGHT = 4.8f
    }
}
