package com.nopalsoft.zombiedash.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiedash.Achievements
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.scene2d.NumItemsBar
import com.nopalsoft.zombiedash.scene2d.OverlayTutorial
import com.nopalsoft.zombiedash.scene2d.ProgressBarUI
import com.nopalsoft.zombiedash.scene2d.VentanaGameover
import com.nopalsoft.zombiedash.scene2d.VentanaPause
import com.nopalsoft.zombiedash.scene2d.VentanaRate
import com.nopalsoft.zombiedash.scene2d.VentanaRevive
import com.nopalsoft.zombiedash.screens.Screens

class GameScreen(game: MainZombieDash) : Screens(game) {
    var oWorld: WorldGame
    var btJump: Button
    var btFire: Button
    var state: Int
    var renderer: WorldGameRenderer2
    var didJump: Boolean = false
    var didFire: Boolean = false
    var isJumpPressed: Boolean = false

    var btPause: Button

    var ventanaPause: VentanaPause

    var lifeBar: ProgressBarUI
    var shieldBar: ProgressBarUI
    var numGemsBar: NumItemsBar
    var numBulletsBar: NumItemsBar

    var lbDistance: Label

    var numRevives: Int

    var overlayTutorial: OverlayTutorial

    init {
        Achievements.tryAgainAchievements()

        when (MathUtils.random(3)) {
            0 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music1.mp3"))
            1 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music2.mp3"))
            2 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/DST-Legends.mp3"))
            3 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/DST-Robotical.mp3"))
        }

        music!!.isLooping = true

        if (Settings.isMusicOn) music!!.play()

        Settings.numeroVecesJugadas++

        state = STATE_RUNNING
        numRevives = 0

        ventanaPause = VentanaPause(this)

        oWorld = WorldGame()
        renderer = WorldGameRenderer2(batcher, oWorld)

        lifeBar = ProgressBarUI(Assets.redBar, Assets.itemHeart, oWorld.oHero!!.vidas.toFloat(), 20f, 440f)
        shieldBar = ProgressBarUI(Assets.whiteBar, Assets.itemShield, oWorld.oHero!!.MAX_SHIELD.toFloat(), oWorld.oHero!!.shield.toFloat(), 20f, 395f)
        numGemsBar = NumItemsBar(Assets.itemGem, 20f, 350f)
        numBulletsBar = NumItemsBar(Assets.weaponSmall, 20f, 305f)

        lbDistance = Label("0 m", Assets.labelStyleGrande)
        lbDistance.setAlignment(Align.center)
        lbDistance.setPosition(SCREEN_WIDTH / 2f - lbDistance.getWidth() / 2f, 445f)

        btJump = Button(Assets.btJump)
        btJump.setSize(Settings.buttonSize, Settings.buttonSize)
        btJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY)
        btJump.getColor().a = .5f
        addEfectoPress(btJump)
        btJump.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                didJump = true
                isJumpPressed = true
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                isJumpPressed = false
                super.touchUp(event, x, y, pointer, button)
            }
        })

        btFire = Button(Assets.btFire)
        btFire.setSize(Settings.buttonSize, Settings.buttonSize)
        btFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY)
        btFire.getColor().a = .5f
        addEfectoPress(btFire)
        btFire.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                didFire = true
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        btPause = Button(Assets.btPause)
        btPause.setSize(45f, 45f)
        btPause.setPosition((SCREEN_WIDTH - 50).toFloat(), (SCREEN_HEIGHT - 50).toFloat())
        addEfectoPress(btPause)
        btPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                setPaused()
            }
        })

        stage.addActor(lifeBar)
        stage.addActor(shieldBar)
        stage.addActor(numGemsBar)
        stage.addActor(numBulletsBar)
        stage.addActor(lbDistance)
        stage.addActor(btFire)
        stage.addActor(btJump)
        stage.addActor(btPause)

        if (Settings.numeroVecesJugadas % 7 == 0 && !Settings.didRate) {
            setPaused()
            VentanaRate(this).show(stage)
        }

        overlayTutorial = OverlayTutorial(this)
        if (Settings.showHelp) {
            overlayTutorial.show(stage)
            Settings.showHelp = false
        }
    }

    override fun update(delta: Float) {
        if (state == STATE_RUNNING) {
            updateRunning(delta)
        }
    }

    private fun updateRunning(delta: Float) {
        if (overlayTutorial.isVisible) return

        oWorld.update(delta, didJump, isJumpPressed, didFire)

        lifeBar.updateActualNum(oWorld.oHero!!.vidas.toFloat())
        shieldBar.updateActualNum(oWorld.oHero!!.shield.toFloat())
        numGemsBar.updateNumGems(oWorld.gems)
        numBulletsBar.updateNumGems(Settings.numBullets)
        lbDistance.setText(oWorld.distance.toInt().toString() + " m")
        Achievements.distance(oWorld.distance.toInt(), oWorld.oHero!!.didGetHurtAtLeastOnce)

        if (oWorld.state == WorldGame.Companion.STATE_GAMEOVER) {
            checkRevive()
        }

        didJump = false
        didFire = didJump
    }

    override fun draw(delta: Float) {
        if (state == STATE_RUNNING) Assets.parallaxBackground!!.render(delta)
        else Assets.parallaxBackground!!.render(0f)
        renderer.render()

        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)

        batcher.begin()
        // Assets.fontGrande.draw(batcher, Gdx.graphics.getFramesPerSecond() + "", 10, 200);
        batcher.end()
    }

    private fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            ventanaPause.show(stage)
        }
    }

    fun setGameover() {
        state = STATE_GAME_OVER
        Settings.changeHighestScore(oWorld.distance.toInt())
        VentanaGameover(this).show(stage)
    }

    fun setRunning() {
        state = STATE_RUNNING
    }

    fun checkRevive() {
        state = STATE_CHECK_REVIVE
        val price = (numRevives + 1) * 500

        if (price < Settings.gemsTotal) {
            VentanaRevive(this, price).show(stage)
        } else {
            setGameover()
        }
    }

    fun setRevive() {
        numRevives++
        state = STATE_RUNNING
        oWorld.state = WorldGame.Companion.STATE_RUNNING
        oWorld.oHero!!.revive()
    }

    override fun hide() {
        super.hide()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.SPACE -> {
                didJump = true
                isJumpPressed = true
                return true
            }

            Input.Keys.F -> {
                didFire = true
                return true
            }

            Input.Keys.ESCAPE, Input.Keys.BACK -> {
                if (ventanaPause.isVisible) ventanaPause.hide()
                else setPaused()
                return true
            }

            else -> return false
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE) {
            isJumpPressed = false
            return true
        }
        return false
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_GAME_OVER: Int = 1
        const val STATE_CHECK_REVIVE: Int = 2
        const val STATE_PAUSED: Int = 3
    }
}
