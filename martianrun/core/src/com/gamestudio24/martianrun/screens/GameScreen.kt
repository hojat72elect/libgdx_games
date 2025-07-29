package com.gamestudio24.martianrun.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.gamestudio24.martianrun.stages.GameStage

class GameScreen : Screen {
    private val stage = GameStage()

    override fun render(delta: Float) {
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        //Update the stage
        stage.draw()
        stage.act(delta)
    }

    override fun resize(width: Int, height: Int) {
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
}
