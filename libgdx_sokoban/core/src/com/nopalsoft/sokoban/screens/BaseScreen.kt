package com.nopalsoft.sokoban.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.MainSokoban
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.game.GameScreen
import kotlin.math.abs

/**
 * The basic screen of the game. All the other screen have to extend this.
 *  Provides a base structure for different screens in a game and handles common functionality
 *  like rendering, input handling, and lifecycle events.
 */
abstract class BaseScreen(val game: MainSokoban) : InputAdapter(), Screen, GestureListener {

    private val camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)
    protected val batcher = game.batcher
    protected val stage = game.stage

    init {
        stage?.clear()
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0F)

        val detector = GestureDetector(20f, .5f, 2f, .15f, this)
        val input = InputMultiplexer(this, detector, stage)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batcher?.projectionMatrix = camera.combined
        draw(delta)

        stage?.act(delta)
        stage?.draw()
    }

    /**
     *  Is used for  changing the screen with a fade-out effect. First creates a black image,
     *  then creates a fade-in and fade-out action to it; and finally adds it to the stage.
     */
    fun changeScreenWithFadeOut(newScreen: Class<*>, level: Int, game: MainSokoban) {
        val blackFadeOut = Image(Assets.pixelBlack)
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT)
        blackFadeOut.color.a = 0f
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) {
                Assets.loadTiledMap(level)
                game.screen = GameScreen(game, level)
            } else if (newScreen == MainMenuScreen::class.java) game.screen = MainMenuScreen(game)
        }))
        stage?.addActor(blackFadeOut)
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>, game: MainSokoban) {
        changeScreenWithFadeOut(newScreen, -1, game)
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
        stage?.viewport?.update(width, height, true)
    }

    override fun show() {}

    override fun hide() {
        Settings.save()
    }

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        stage?.dispose()
        batcher?.dispose()
    }

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int) = false
    override fun tap(x: Float, y: Float, count: Int, button: Int) = false
    override fun longPress(x: Float, y: Float) = false

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

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float) = false
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int) = false
    override fun zoom(initialDistance: Float, distance: Float) = false
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?) = false

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

    override fun pinchStop() {}

    companion object {

        const val SCREEN_WIDTH = 800F
        const val SCREEN_HEIGHT = 480F
    }
}