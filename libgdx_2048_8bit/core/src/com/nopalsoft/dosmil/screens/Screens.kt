package com.nopalsoft.dosmil.screens

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
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.MainGame
import com.nopalsoft.dosmil.Settings
import com.nopalsoft.dosmil.game.GameScreen
import kotlin.math.abs

abstract class Screens(game: MainGame) : InputAdapter(), Screen, GestureListener {

    var game: MainGame
    var camera: OrthographicCamera
    var batcher: SpriteBatch?
    var stage: Stage? = game.stage
    var blackFadeOut: Image? = null

    init {
        stage!!.clear()
        this.batcher = game.batcher
        this.game = game

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f

        val detector = GestureDetector(20f, .5f, 2f, .15f, this)

        val input = InputMultiplexer(this, detector, stage)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batcher!!.projectionMatrix = camera.combined
        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>, game: MainGame) {
        blackFadeOut = Image(Assets.pixelBlack)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.color.a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) game.screen = GameScreen(game)
            else if (newScreen == MainMenuScreen::class.java) game.screen = MainMenuScreen(game)
            else if (newScreen == HelpScreen::class.java) game.screen = HelpScreen(game)
        }))
        stage!!.addActor(blackFadeOut)
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

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        Settings.save()
    }

    override fun pause() {
        Assets.pauseMusic()
    }

    override fun resume() {
        Assets.playMusic()
    }

    override fun dispose() {
        stage!!.dispose()
        batcher!!.dispose()
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

    override fun pinch(initialPointer1: Vector2, initialPointer2: Vector2, pointer1: Vector2, pointer2: Vector2): Boolean {
        return false
    }

    open fun up() {
    }

    open fun down() {
    }

    open fun left() {
    }

    open fun right() {
    }

    override fun pinchStop() {
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800
    }
}