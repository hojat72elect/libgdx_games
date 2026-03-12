package com.nopalsoft.donttap.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.DoNotTapGame
import com.nopalsoft.donttap.Settings.save
import com.nopalsoft.donttap.game.GameScreen

abstract class Screens(game: DoNotTapGame) : InputAdapter(), Screen {
    @JvmField
    var game: DoNotTapGame?

    var camera: OrthographicCamera
    var batch: SpriteBatch

    @JvmField
    var stage: Stage = game.stage

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batch.setProjectionMatrix(camera.combined)
        draw(delta)

        stage.act(delta)
        stage.draw()
    }

    var blackFadeOut: Image? = null

    init {
        this.stage.clear()
        this.batch = game.batch
        this.game = game

        camera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val input = InputMultiplexer(this, stage)
        Gdx.input.setInputProcessor(input)
    }

    @JvmOverloads
    fun changeScreenWithFadeOut(
        newScreen: Class<*>?,
        game: DoNotTapGame, mode: Int = -1
    ) {
        blackFadeOut = Image(Assets.blackPixel)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    when (newScreen) {
                        SelectScreen::class.java -> game.setScreen(SelectScreen(game))
                        GameScreen::class.java -> game.setScreen(GameScreen(game, mode))
                        MainMenuScreen::class.java -> game.setScreen(MainMenuScreen(game))
                    }
                }
            )
        )
        stage.addActor(blackFadeOut)
    }

    fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.getX(), actor.getY() - 3)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.getX(), actor.getY() + 3)
            }
        })
    }

    protected fun addBackGround() {
        val tabBackground = Table()
        tabBackground.setFillParent(true)
        tabBackground.defaults().expand().uniform().fill()

        for (col in 0..3) {
            val tileColorPos = MathUtils.random(3)
            for (ren in 0..3) {
                var img = Image(Assets.whiteTile)

                if (tileColorPos == ren) {
                    val colorTile = MathUtils.random(4)
                    when (colorTile) {
                        0 -> img = Image(Assets.yellowTile)
                        1 -> img = Image(Assets.blueTile)
                        2 -> img = Image(Assets.redTile)
                        3 -> img = Image(Assets.purpleTile)
                        4 -> img = Image(Assets.orangeTile)
                    }
                }
                tabBackground.add(img)
            }
            tabBackground.row()
        }
        stage.addActor(tabBackground)
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        save()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
    }

    companion object {
        const val SCREEN_WIDTH = 480
        const val SCREEN_HEIGHT = 800

        const val WORLD_WIDTH = 480f
        const val WORLD_HEIGHT = 720F
    }
}
