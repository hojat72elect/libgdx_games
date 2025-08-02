package com.nopalsoft.fifteen.screens

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
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.Assets.pauseMusic
import com.nopalsoft.fifteen.Assets.playMusic
import com.nopalsoft.fifteen.MainFifteen
import com.nopalsoft.fifteen.Settings.save
import com.nopalsoft.fifteen.game.GameScreen
import kotlin.math.abs

abstract class Screens(game: MainFifteen) : InputAdapter(), Screen, GestureListener {

    var game: MainFifteen?
    var oCam: OrthographicCamera
    var batcher: SpriteBatch?
    var stage = game.stage
    var blackFadeOut: Image? = null

    init {
        this.stage!!.clear()
        this.batcher = game.batcher
        this.game = game

        oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val detector = GestureDetector(20f, .5f, 2f, .15f, this)

        val input = InputMultiplexer(this, detector, stage)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        oCam.update()
        batcher!!.setProjectionMatrix(oCam.combined)
        draw(delta)

        stage!!.act(delta)
        stage!!.draw()
    }

    fun changeScreenWithFadeOut(
        newScreen: Class<*>?,
        game: MainFifteen
    ) {
        blackFadeOut = Image(Assets.pixelNegro)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    when (newScreen) {
                        GameScreen::class.java -> game.setScreen(GameScreen(game))
                        MainMenuScreen::class.java -> game.setScreen(MainMenuScreen(game))
                        HelpScreen::class.java -> game.setScreen(HelpScreen(game))
                    }
                }
            )
        )
        stage!!.addActor(blackFadeOut)
    }

    fun addEfectoPress(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.getX(), actor.getY() - 5)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.getX(), actor.getY() + 5)
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
        save()
    }

    override fun pause() {
        pauseMusic()
    }

    override fun resume() {
        playMusic()
    }

    override fun dispose() {
        stage!!.dispose()
        batcher!!.dispose()
    }

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        if (abs(velocityX) > abs(velocityY)) {
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
        // TODO Auto-generated method stub
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun pinch(
        initialPointer1: Vector2?, initialPointer2: Vector2?,
        pointer1: Vector2?, pointer2: Vector2?
    ): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun pinchStop() {
    }

    open fun up() {
        Gdx.app.log("UP", "")
    }

    open fun down() {
        Gdx.app.log("DOWN", "")
    }

    open fun left() {
        Gdx.app.log("LEFT", "")
    }

    open fun right() {
        Gdx.app.log("RIGHT", "")
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800
    }
}