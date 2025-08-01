package com.nopalsoft.zombiedash.screens

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
import com.nopalsoft.zombiedash.AnimationSprite
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.game.GameScreen
import com.nopalsoft.zombiedash.objects.Hero
import com.nopalsoft.zombiedash.scene2d.AnimatedSpriteActor

abstract class Screens(game: MainZombieDash) : InputAdapter(), Screen {
    @JvmField
    var game: MainZombieDash?

    @JvmField
    var oCam: OrthographicCamera

    @JvmField
    var batcher: SpriteBatch

    @JvmField
    var stage: Stage

    @JvmField
    protected var music: Music? = null
    var blackFadeOut: Image? = null

    init {
        this.stage = game.stage!!
        this.stage.clear()
        this.batcher = game.batcher!!
        this.game = game

        oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)

        val input = InputMultiplexer(stage, this)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)
        stage.act(delta)

        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(delta)
        stage.draw()
    }

    fun addEfectoPress(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.getX(), actor.getY() - 5)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.getX(), actor.getY() + 5)
            }
        })
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>?, game: MainZombieDash) {
        blackFadeOut = Image(Assets.pixelNegro)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(object : Runnable {
            override fun run() {
                if (newScreen == GameScreen::class.java) {
                    game.setScreen(GameScreen(game))
                } else if (newScreen == MainMenuScreen::class.java) game.setScreen(MainMenuScreen(game))
                else if (newScreen == SettingsScreen::class.java) game.setScreen(SettingsScreen(game))

                // El blackFadeOut se remueve del stage cuando se le da new Screens(game) "Revisar el constructor de la clase Screens" por lo que no hay necesidad de hacer
                // blackFadeout.remove();
            }
        })))

        val lbl = Label(game.idiomas!!.get("loading"), Assets.labelStyleGrande)
        lbl.setPosition(SCREEN_WIDTH / 2f - lbl.getWidth() / 2f, SCREEN_HEIGHT / 2f - lbl.getHeight() / 2f)
        lbl.getColor().a = 0f
        lbl.addAction(Actions.fadeIn(.6f))

        var heroRun: AnimationSprite? = null
        when (Settings.skinSeleccionada) {
            Hero.TIPO_FORCE -> heroRun = Assets.heroForceRun
            Hero.TIPO_RAMBO -> heroRun = Assets.heroRamboRun
            Hero.TIPO_SOLDIER -> heroRun = Assets.heroSoldierRun
            Hero.TIPO_SWAT -> heroRun = Assets.heroSwatRun
            Hero.TIPO_VADER -> heroRun = Assets.heroVaderRun
        }

        val corriendo = AnimatedSpriteActor(heroRun!!)
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
        const val SCREEN_WIDTH: Int = 800
        const val SCREEN_HEIGHT: Int = 480

        const val WORLD_WIDTH: Float = 8f
        const val WORLD_HEIGHT: Float = 4.8f
    }
}
