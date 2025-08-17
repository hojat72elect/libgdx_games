package com.nopalsoft.zombiewars.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.zombiewars.Assets
import com.nopalsoft.zombiewars.MainZombieWars
import com.nopalsoft.zombiewars.Settings
import com.nopalsoft.zombiewars.game.GameScreen
import com.nopalsoft.zombiewars.scene2d.AnimatedSpriteActor

abstract class Screens(val game: MainZombieWars) : InputAdapter(), Screen {

    var oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
    var batcher = game.batcher
    var stage = game.stage

    protected var music: Music? = null
    var blackFadeOut: Image? = null

    init {
        this.stage.clear()
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)
        val input = InputMultiplexer(stage, this)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        var tempDelta = delta
        if (tempDelta > 0.1F) tempDelta = 0.1F

        update(tempDelta)
        stage.act(tempDelta)
        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(tempDelta)
        stage.draw()
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>, game: MainZombieWars) {
        blackFadeOut = Image(Assets.pixelNegro)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(0.5F), Actions.run {
            if (newScreen == GameScreen::class.java) {
                Assets.loadTiledMap()
                game.setScreen(GameScreen(game))
            }
        }))

        val lbl = Label("Loading..", Assets.labelStyleGrande)
        lbl.setPosition(SCREEN_WIDTH / 2f - lbl.getWidth() / 2f, SCREEN_HEIGHT / 2f - lbl.getHeight() / 2f)
        lbl.getColor().a = 0f
        lbl.addAction(Actions.fadeIn(0.6F))

        val corriendo = AnimatedSpriteActor(Assets.zombieKidWalk!!)
        corriendo.setSize(70f, 70f)
        corriendo.setPosition(SCREEN_WIDTH / 2f - corriendo.getWidth() / 2f, 250f)

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
        const val SCREEN_WIDTH = 800
        const val SCREEN_HEIGHT = 480
        const val WORLD_WIDTH = 8F
        const val WORLD_HEIGHT = 4.8F
    }
}
