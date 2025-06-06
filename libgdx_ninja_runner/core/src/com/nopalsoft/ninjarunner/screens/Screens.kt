package com.nopalsoft.ninjarunner.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.NinjaRunnerGame
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.shop.ShopScreen
import kotlin.math.abs

abstract class Screens(_game: Game?) : InputAdapter(), Screen, GestureListener {
    var game: NinjaRunnerGame = _game as NinjaRunnerGame

    var camera: OrthographicCamera
    var batch: SpriteBatch?
    var stage = game.stage

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(.191f, .703f, .996f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batch!!.setProjectionMatrix(camera.combined)
        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    var blackFadeOut: Image? = null

    init {
        this.stage!!.clear()
        this.batch = game.batch


        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val detector = GestureDetector(20f, .5f, 2f, .15f, this)

        val input = InputMultiplexer(this, detector, stage)
        Gdx.input.inputProcessor = input
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>?, game: NinjaRunnerGame) {
        blackFadeOut = Image(Assets.blackPixelDrawable)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(Runnable {
            if (newScreen == GameScreen::class.java) {
                game.setScreen(GameScreen(game, true))
            } else if (newScreen == SettingsScreen::class.java) game.setScreen(SettingsScreen(game))
            else if (newScreen == ShopScreen::class.java) game.setScreen(ShopScreen(game))
        })))
        stage!!.addActor(blackFadeOut)
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        stage!!.dispose()
        batch!!.dispose()
    }

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        if (abs(velocityX.toDouble()) > abs(velocityY.toDouble())) {
            if (velocityX > 0) {
                right()
            } else {
                left()
            }
        } else {
            if (velocityY > 0) {
                down()
            } else {
                up()
            }
        }
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        return false
    }

    override fun pinchStop() {
    }

    fun up() {
        Gdx.app.log("UP", "")
    }

    fun down() {
        Gdx.app.log("DOWN", "")
    }

    fun left() {
        Gdx.app.log("LEFT", "")
    }

    open fun right() {
        Gdx.app.log("RIGHT", "")
    }

    companion object {
        const val SCREEN_WIDTH: Int = 800
        const val SCREEN_HEIGHT: Int = 480
        const val WORLD_WIDTH: Float = 8f
        const val WORLD_HEIGHT: Float = 4.8f
    }
}