package com.nopalsoft.flappy.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.flappy.Assets
import com.nopalsoft.flappy.Assets.getTextWidth
import com.nopalsoft.flappy.MainFlappyBird
import com.nopalsoft.flappy.screens.Screens

class GameScreen(game: MainFlappyBird) : Screens(game) {

    private var state = STATE_READY
    private val oWorld = WorldGame()
    private val renderer = WorldGameRenderer(spriteBatch, oWorld)
    private val getReady = Image(Assets.getReady)
    private val tap = Image(Assets.tap)
    private val gameOver = Image(Assets.gameOver)

    init {
        getReady.setPosition(SCREEN_WIDTH / 2f - getReady.width / 2f, 600f)
        tap.setPosition(SCREEN_WIDTH / 2f - tap.width / 2f, 310f)
        gameOver.setPosition(SCREEN_WIDTH / 2f - getReady.width / 2f, 350f)

        stage.addActor(getReady)
        stage.addActor(tap)
    }

    override fun update(delta: Float) {
        when (state) {
            STATE_READY -> updateReady()
            STATE_RUNNING -> updateRunning(delta)
            STATE_GAME_OVER -> updateGameOver()
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched()) {
            getReady.addAction(Actions.fadeOut(.3f))
            tap.addAction(
                Actions.sequence(
                    Actions.fadeOut(.3f),
                    Actions.run {
                        getReady.remove()
                        tap.remove()
                        state = STATE_RUNNING
                    })
            )
        }
    }

    private fun updateRunning(delta: Float) {
        val jump = Gdx.input.justTouched()

        oWorld.update(delta, jump)

        if (oWorld.state == WorldGame.STATE_GAME_OVER) {
            state = STATE_GAME_OVER
            stage.addActor(gameOver)
        }
    }

    private fun updateGameOver() {
        if (Gdx.input.justTouched()) {
            gameOver.addAction(
                Actions.sequence(
                    Actions.fadeOut(.3f),
                    Actions.run {
                        gameOver.remove()
                        game.screen = GameScreen(game)
                    })
            )
        }
    }

    override fun draw(delta: Float) {
        renderer.render()

        camera.update()
        spriteBatch.projectionMatrix = camera.combined


        spriteBatch.begin()
        val width = getTextWidth(oWorld.score.toString() + "")
        Assets.font.draw(spriteBatch, oWorld.score.toString() + "", SCREEN_WIDTH / 2f - width / 2f, 700f)
        spriteBatch.end()
    }

    companion object {
        const val STATE_READY = 0
        const val STATE_RUNNING = 1
        const val STATE_GAME_OVER = 2
    }
}
