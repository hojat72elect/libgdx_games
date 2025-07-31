package com.nopalsoft.zombiekiller.game

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.zombiekiller.Achievements
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.scene2d.DialogGameover
import com.nopalsoft.zombiekiller.scene2d.DialogNextLevel
import com.nopalsoft.zombiekiller.scene2d.DialogPause
import com.nopalsoft.zombiekiller.scene2d.DialogRate
import com.nopalsoft.zombiekiller.scene2d.NumGemsBar
import com.nopalsoft.zombiekiller.scene2d.OverlayTutorial
import com.nopalsoft.zombiekiller.scene2d.ProgressBarUI
import com.nopalsoft.zombiekiller.scene2d.SkullBar
import com.nopalsoft.zombiekiller.scene2d.TouchPadControls
import com.nopalsoft.zombiekiller.screens.Screens

class GameScreen(game: MainZombie, level: Int) : Screens(game) {
    var worldGame: WorldGame
    var buttonJump: Button
    var buttonFire: Button
    var touchpad: Touchpad
    var touchPadControls: TouchPadControls
    var level: Int
    var state: Int
    var renderer: WorldGameRenderer2
    var accelerationX: Float = 0f
    var accelerationY: Float = 0f
    var didJump: Boolean = false
    var isFiring: Boolean = false
    var buttonPause: Button
    var gameOverDialog: DialogGameover
    var pauseDialog: DialogPause
    var overlayTutorial: OverlayTutorial
    var labelLevel: Label?

    var lifeBar: ProgressBarUI
    var shieldBar: ProgressBarUI
    var numGemsBar: NumGemsBar
    var skullBar: SkullBar

    init {
        if (music != null) {
            music.stop()
            music.dispose()
            music = null
        }

        when (MathUtils.random(3)) {
            0 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/music1.mp3"))
            1 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/music2.mp3"))
            2 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/DST-Legends.mp3"))
            3 -> music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/DST-Robotical.mp3"))
        }

        music.isLooping = true

        if (Settings.isMusicOn) music.play()

        this.level = level
        Gdx.app.log("Map", level.toString() + "")
        labelLevel = Label(game.idiomas!!.get("world") + " " + (level + 1), Assets.labelStyleChico)
        labelLevel!!.setPosition(SCREEN_WIDTH / 2f - labelLevel!!.getWidth() / 2f, 391f)

        state = STATE_RUNNING
        Settings.numeroVecesJugadas++

        gameOverDialog = DialogGameover(this)
        pauseDialog = DialogPause(this)

        worldGame = WorldGame()
        renderer = WorldGameRenderer2(batcher, worldGame)

        lifeBar = ProgressBarUI(Assets.redBar, Assets.itemHeart, worldGame.hero!!.lives.toFloat(), 20f, 440f)
        shieldBar = ProgressBarUI(Assets.whiteBar, Assets.itemShield, worldGame.hero!!.MAX_SHIELDS.toFloat(), worldGame.hero!!.shield.toFloat(), 20f, 395f)
        numGemsBar = NumGemsBar(Assets.itemGem, 20f, 350f)
        skullBar = SkullBar(SCREEN_WIDTH / 2f, 415f)

        buttonJump = Button(Assets.btUp)
        buttonJump.setSize(Settings.buttonSize, Settings.buttonSize)
        buttonJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY)
        buttonJump.getColor().a = .5f
        addPressEffect(buttonJump)
        buttonJump.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                didJump = true
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        buttonFire = Button(Assets.btFire)
        buttonFire.setSize(Settings.buttonSize, Settings.buttonSize)
        buttonFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY)
        buttonFire.getColor().a = .5f
        addPressEffect(buttonFire)
        buttonFire.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isFiring = true
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        buttonPause = Button(Assets.btPause)
        buttonPause.setSize(45f, 45f)
        buttonPause.setPosition((SCREEN_WIDTH - 50).toFloat(), (SCREEN_HEIGHT - 50).toFloat())
        addPressEffect(buttonPause)
        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                setPaused()
            }
        })

        touchpad = Touchpad(5f, Assets.touchPadStyle)
        touchpad.setPosition(Settings.padPositionX, Settings.padPositionY)
        touchpad.setSize(Settings.padSize, Settings.padSize)
        touchpad.getColor().a = .5f

        touchPadControls = TouchPadControls()
        touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY)
        touchPadControls.getColor().a = .5f

        if (Gdx.app.type == ApplicationType.Android || Gdx.app.type == ApplicationType.iOS) {
            stage.addActor(buttonFire)
            stage.addActor(buttonJump)

            if (Settings.isPadEnabled) stage.addActor(touchpad)
            else stage.addActor(touchPadControls)
        }
        stage.addActor(buttonPause)
        stage.addActor(lifeBar)
        stage.addActor(shieldBar)
        stage.addActor(numGemsBar)
        stage.addActor(skullBar)
        stage.addActor(labelLevel)

        overlayTutorial = OverlayTutorial(this)
        if (level == 0) {
            overlayTutorial.show(stage)
        }

        if (Settings.numeroVecesJugadas % 7 == 0 && !Settings.didRate) {
            setPaused()
            DialogRate(this).show(stage)
        }
    }

    override fun update(delta: Float) {
        if (state == STATE_RUNNING) {
            updateRunning(delta)
        }
    }

    private fun updateRunning(delta: Float) {
        if (overlayTutorial.isVisible) return

        val sensibility = .6f

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || touchpad.knobPercentX < -sensibility || touchPadControls.isMovingLeft) accelerationX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || touchpad.knobPercentX > sensibility || touchPadControls.isMovingRight) accelerationX = 1f
        else accelerationX = 0f

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || touchpad.knobPercentY > sensibility || touchPadControls.isMovingUp) accelerationY = 1f
        else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || touchpad.knobPercentY < -sensibility || touchPadControls.isMovingDown) accelerationY = -1f
        else accelerationY = 0f

        if (Gdx.input.isKeyPressed(Input.Keys.F)) isFiring = true

        worldGame.update(delta, didJump, isFiring, accelerationX, accelerationY)

        lifeBar.updateActualNum(worldGame.hero!!.lives.toFloat())
        shieldBar.updateActualNum(worldGame.hero!!.shield.toFloat())
        numGemsBar.updateNumGems(worldGame.gems)
        skullBar.tryToUpdateSkulls(worldGame.skulls)

        if (worldGame.state == WorldGame.Companion.STATE_GAMEOVER) {
            setGameover()
        } else if (worldGame.state == WorldGame.Companion.STATE_NEXT_LEVEL) {
            setNextLevel()
        }

        didJump = false
        isFiring = didJump
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        gameOverDialog.show(stage)
    }

    private fun setNextLevel() {
        state = STATE_NEXT_LEVEL
        Settings.saveLevel(level, worldGame.skulls)

        Achievements.unlockCollectedSkulls()

        DialogNextLevel(this).show(stage)
    }

    private fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            pauseDialog.show(stage)
        }
    }

    override fun hide() {
        Settings.zombiesKilled += worldGame.totalZombiesKilled.toLong()
        Achievements.unlockKilledZombies()
        super.hide()
    }

    fun setRunning() {
        state = STATE_RUNNING
    }

    override fun keyUp(keycode: Int): Boolean {
        return super.keyUp(keycode)
    }

    override fun draw(delta: Float) {
        renderer.render()

        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)
        batcher.begin()
        batcher.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.X || keycode == Input.Keys.C || keycode == Input.Keys.V || keycode == Input.Keys.B || keycode == Input.Keys.N || keycode == Input.Keys.M) {
            didJump = true
            return true
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (pauseDialog.isVisible) pauseDialog.hide()
            else setPaused()
            return true
        }
        return false
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_GAME_OVER: Int = 1
        const val STATE_NEXT_LEVEL: Int = 2
        const val STATE_PAUSED: Int = 3
    }
}
