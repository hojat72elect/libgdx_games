package com.nopalsoft.zombiewars.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.zombiewars.Assets
import com.nopalsoft.zombiewars.MainZombieWars
import com.nopalsoft.zombiewars.game.GameScreen
import com.nopalsoft.zombiewars.scene2d.AnimatedSpriteActor

abstract class Screens(game: MainZombieWars) : InputAdapter(), Screen {
    var game: MainZombieWars

    @JvmField
    var oCam: OrthographicCamera
    @JvmField
    var batcher: SpriteBatch
    var stage = game.stage!!

    protected var music: Music? = null
    var blackFadeOut: Image? = null

    init {
        stage.clear()
        this.batcher = game.batcher!!
        this.game = game

        oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        oCam.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f

        val input = InputMultiplexer(stage, this)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)
        stage.act(delta)

        oCam.update()
        batcher.projectionMatrix = oCam.combined

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(delta)
        stage.draw()
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>, game: MainZombieWars) {
        blackFadeOut = Image(Assets.pixelNegro)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.color.a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) {
                Assets.loadTiledMap()
                game.screen = GameScreen(game)
            }
        }))

        val lbl = Label("Loading..", Assets.labelStyleGrande)
        lbl.setPosition(SCREEN_WIDTH / 2f - lbl.width / 2f, SCREEN_HEIGHT / 2f - lbl.height / 2f)
        lbl.color.a = 0f
        lbl.addAction(Actions.fadeIn(.6f))

        val corriendo = AnimatedSpriteActor(Assets.zombieKidWalk)
        corriendo.setSize(70f, 70f)
        corriendo.setPosition(SCREEN_WIDTH / 2f - corriendo.width / 2f, 250f)

        stage.addActor(blackFadeOut)
        stage.addActor(corriendo)
        stage.addActor(lbl)
    }

    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        if (music != null) {
            music!!.stop()
            music!!.dispose()
            music = null
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        batcher.dispose()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 800
        const val SCREEN_HEIGHT: Int = 480

        const val WORLD_WIDTH: Float = 8f
        const val WORLD_HEIGHT: Float = 4.8f
    }
}
