package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.GalaxyInvadersGame
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.Settings.save
import com.nopalsoft.invaders.game.GameScreen

abstract class Screens(game: GalaxyInvadersGame) : InputAdapter(), Screen {
    @JvmField
    var game: GalaxyInvadersGame

    var camera: OrthographicCamera

    @JvmField
    var batch: SpriteBatch?

    @JvmField
    var stage: Stage? = game.stage
    private var assetManager: Assets?

    init {
        stage!!.clear()
        this.game = game
        assetManager = game.assetManager

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f
        batch = game.batch

        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input

        Assets.font10!!.data.setScale(.65f)
        Assets.font15!!.data.setScale(1f)
        Assets.font45!!.data.setScale(.85f)
        Assets.font60!!.data.setScale(1.2f)
        when (this) {
            is MainMenuScreen -> {
                Assets.font10!!.data.setScale(.65f)
                Assets.font15!!.data.setScale(1f)
                Assets.font45!!.data.setScale(.85f)
                Assets.font60!!.data.setScale(1.2f)
            }

            is GameScreen -> {
                Assets.font15!!.data.setScale(1f)
                Assets.font45!!.data.setScale(.7f)
                Assets.font10!!.data.setScale(.65f)
            }

            is SettingsScreen -> {
                Assets.font10!!.data.setScale(1f)
                Assets.font15!!.data.setScale(1f)
                Assets.font45!!.data.setScale(.65f)
                Assets.font60!!.data.setScale(1f)
            }
        }
    }

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        save()
    }

    override fun pause() {
        Assets.music!!.pause()
    }

    override fun resume() {
        if (Settings.musicEnabled && !Assets.music!!.isPlaying) Assets.music!!.play()
    }

    override fun dispose() {
        stage!!.dispose()
        batch!!.dispose()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 320
        const val SCREEN_HEIGHT: Int = 480

        const val WORLD_SCREEN_WIDTH: Int = 32
        const val WORLD_SCREEN_HEIGHT: Int = 48
    }
}
