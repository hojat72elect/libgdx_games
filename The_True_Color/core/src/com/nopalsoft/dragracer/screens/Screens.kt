package com.nopalsoft.dragracer.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.Settings.save
import com.nopalsoft.dragracer.game.GameScreen
import com.nopalsoft.dragracer.shop.ShopScreen
import kotlin.math.abs

abstract class Screens(val game: MainStreet) : InputAdapter(), Screen, GestureListener {


    private var camera: OrthographicCamera
    var batch: SpriteBatch?
    var stage = game.stage


    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batch!!.projectionMatrix = camera.combined
        draw(delta)

        stage?.act(delta)
        stage!!.draw()
    }

    private var blackFadeOut: Image? = null

    init {
        stage!!.clear()
        this.batch = game.batch


        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f

        val detector = GestureDetector(20f, .5f, 2f, .15f, this)

        val input = InputMultiplexer(this, detector, stage)
        Gdx.input.setInputProcessor(input)
    }

    fun changeScreenWithFadeOut(
        newScreen: Class<*>,
        game: MainStreet
    ) {
        blackFadeOut = Image(Assets.blackPixel)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.color.a = 0f
        blackFadeOut!!.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    when (newScreen) {
                        MainMenuScreen::class.java -> game.screen = MainMenuScreen(game)
                        GameScreen::class.java -> game.screen = GameScreen(game)
                        ShopScreen::class.java -> game.screen = ShopScreen(game)
                    }
                    // The blackFadeOut is removed from the stage when new Screens(game) is given "Review the constructor of the Screens class" so there is no need to do.
                })
        )
        stage!!.addActor(blackFadeOut)
    }

    fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 3)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.x, actor.y + 3)
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
        Assets.music!!.pause()
    }

    override fun resume() {
        if (Settings.isMusicOn) Assets.music!!.play()
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

    override fun pinch(
        initialPointer1: Vector2, initialPointer2: Vector2,
        pointer1: Vector2, pointer2: Vector2
    ): Boolean {
        return false
    }

    override fun pinchStop() {
    }

    open fun up() {
    }

    private fun down() {
    }

    open fun left() {
    }

    open fun right() {
    }


    protected fun entranceAction(act: Actor, y: Float, duration: Float) {
        act.addAction(
            Actions.moveTo(
                SCREEN_WIDTH / 2f - act.width / 2f,
                y, duration, Interpolation.exp10
            )
        )
    }

    fun setAnimationChangeColor(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun enter(
                event: InputEvent, x: Float, y: Float, pointer: Int,
                fromActor: Actor?
            ) {
                actor.color = Color.RED
            }

            override fun exit(
                event: InputEvent, x: Float, y: Float, pointer: Int,
                toActor: Actor?
            ) {
                actor.color = Color.WHITE
            }
        })
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800

        const val WORLD_WIDTH: Float = 480f
        const val WORLD_HEIGHT: Float = 800f
    }
}
