package com.nopalsoft.zombiekiller.screens

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
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.zombiekiller.AnimationSprite
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.game.GameScreen
import com.nopalsoft.zombiekiller.game_objects.Hero
import com.nopalsoft.zombiekiller.scene2d.AnimatedSpriteActor

abstract class Screens(game: MainZombie) : InputAdapter(), Screen {
    var game: MainZombie?

    var oCam: OrthographicCamera
    var batcher: SpriteBatch?
    var stage = game.stage

    protected var music: Music? = null
    var blackFadeOut: Image? = null

    init {
        this.stage!!.clear()
        this.batcher = game.batcher
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
        stage!!.act(delta)

        oCam.update()
        batcher!!.setProjectionMatrix(oCam.combined)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(delta)
        stage!!.draw()
    }

    fun addPressEffect(actor: Actor) {
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

    fun changeScreenWithFadeOut(newScreen: Class<*>?, level: Int, game: MainZombie) {
        blackFadeOut = Image(Assets.pixelNegro)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.getColor().a = 0f
        blackFadeOut!!.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            when (newScreen) {
                GameScreen::class.java -> {
                    Assets.loadTiledMap(level)
                    game.setScreen(GameScreen(game, level))
                }

                MainMenuScreen::class.java -> game.setScreen(MainMenuScreen(game))
                SettingsScreen::class.java -> game.setScreen(SettingsScreen(game))
            }
        }))

        val lbl = Label(game.idiomas!!.get("loading"), Assets.labelStyleGrande)
        lbl.setPosition(SCREEN_WIDTH / 2f - lbl.getWidth() / 2f, SCREEN_HEIGHT / 2f - lbl.getHeight() / 2f)
        lbl.getColor().a = 0f
        lbl.addAction(Actions.fadeIn(.6f))

        var heroRun: AnimationSprite? = null
        when (Settings.skinSeleccionada) {
            Hero.TYPE_FORCE, Hero.TYPE_RAMBO -> heroRun = Assets.heroRamboWalk
            Hero.TYPE_SOLDIER -> heroRun = Assets.heroSoldierWalk
            Hero.TYPE_SWAT -> heroRun = Assets.heroSwatWalk
            Hero.TYPE_VADER -> heroRun = Assets.heroVaderWalk
        }
        val runningAnimatedHero = AnimatedSpriteActor(heroRun!!)
        runningAnimatedHero.setSize(70f, 70f)
        runningAnimatedHero.setPosition(SCREEN_WIDTH / 2f - runningAnimatedHero.getWidth() / 2f, 250f)

        stage!!.addActor(blackFadeOut)
        stage!!.addActor(runningAnimatedHero)
        stage!!.addActor(lbl)
    }

    fun changeScreenWithFadeOut(newScreen: Class<*>?, game: MainZombie) {
        changeScreenWithFadeOut(newScreen, -1, game)
    }

    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
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
        batcher!!.dispose()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 800
        const val SCREEN_HEIGHT: Int = 480

        const val WORLD_WIDTH: Float = 8f
        const val WORLD_HEIGHT: Float = 4.8f
    }
}
