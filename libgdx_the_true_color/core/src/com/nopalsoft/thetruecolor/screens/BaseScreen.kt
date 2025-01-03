package com.nopalsoft.thetruecolor.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Settings
import com.nopalsoft.thetruecolor.TrueColorGame
import com.nopalsoft.thetruecolor.game.GameScreen

abstract class BaseScreen(@JvmField val game: TrueColorGame) : InputAdapter(), Screen {

    private val camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)

    @JvmField
    val batcher = game.batcher

    @JvmField
    val stage = game.stage

    init {

        stage.clear()
        camera.position.set(SCREEN_WIDTH / 2F, SCREEN_HEIGHT / 2F, 0F)
        Gdx.input.inputProcessor = InputMultiplexer(this, stage)
    }

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batcher.projectionMatrix = camera.combined
        draw(delta)

        stage.act(delta)
        stage.draw()
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>, game: TrueColorGame) {
        val blackFadeOut = Image(Assets.pixelBlack)
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT)
        blackFadeOut.color.a = 0f
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) game.screen = GameScreen(game)
            else if (newScreen == MainMenuScreen::class.java) game.screen = MainMenuScreen(game)
        }))
        stage.addActor(blackFadeOut)
    }

    fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 5)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.x, actor.y + 5)
            }
        })
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, true)

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
    }

    companion object {

        const val SCREEN_WIDTH = 480F
        const val SCREEN_HEIGHT = 800F
    }
}