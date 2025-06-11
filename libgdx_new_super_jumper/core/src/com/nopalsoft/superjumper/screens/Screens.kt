package com.nopalsoft.superjumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.SuperJumperGame
import com.nopalsoft.superjumper.game.GameScreen

abstract class Screens(game: SuperJumperGame) : InputAdapter(), Screen {
    @JvmField
    var game: SuperJumperGame

    private var camera: OrthographicCamera

    @JvmField
    var batch: SpriteBatch?

    @JvmField
    var stage: Stage? = game.stage

    private var music: Music? = null

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)
        stage!!.act(delta)

        camera.update()
        batch!!.projectionMatrix = camera.combined

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(delta)
        stage!!.draw()
    }

    fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.x, actor.y - 5)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.x, actor.y + 5)
            }
        })
    }

    private var blackFadeOut: Image? = null

    init {
        stage!!.clear()
        this.batch = game.batch
        this.game = game

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f

        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>, game: SuperJumperGame) {
        blackFadeOut = Image(Assets.blackPixel)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.color.a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) {
                game.screen = GameScreen(game)
            } else if (newScreen == MainMenuScreen::class.java) {
                game.screen = MainMenuScreen(game)
            }
        }))

        val label = Label("Loading..", Assets.labelStyleLarge)
        label.setPosition(SCREEN_WIDTH / 2f - label.width / 2f, SCREEN_HEIGHT / 2f - label.height / 2f)
        label.color.a = 0f
        label.addAction(Actions.fadeIn(.6f))

        stage!!.addActor(blackFadeOut)
        stage!!.addActor(label)
    }

    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        if (music != null) {
            music!!.stop()
            music!!.dispose()
            music = null
        }

        Settings.save()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        batch!!.dispose()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800

        const val WORLD_WIDTH: Float = 4.8f
        const val WORLD_HEIGHT: Float = 8f
    }
}
