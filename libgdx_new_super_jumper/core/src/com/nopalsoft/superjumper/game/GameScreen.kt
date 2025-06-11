package com.nopalsoft.superjumper.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.SuperJumperGame
import com.nopalsoft.superjumper.scene2d.BaseWindowGameover
import com.nopalsoft.superjumper.scene2d.BaseWindowPause
import com.nopalsoft.superjumper.screens.Screens

class GameScreen(game: SuperJumperGame) : Screens(game) {
    var state: Int = 0
    var oWorld: WorldGame = WorldGame()
    var renderer: WorldGameRender = WorldGameRender(batch, oWorld)

    var touchPositionWorldCoordinates: Vector3 = Vector3()
    var didFire: Boolean = false

    var labelDistance: Label
    var labelCoins: Label
    var labelBullets: Label

    var buttonPause: Button

    var pauseWindow: BaseWindowPause = BaseWindowPause(this)

    init {
        state = STATE_RUNNING
        Settings.numTimesPlayed++

        val scoresTable = Table()
        scoresTable.setSize(SCREEN_WIDTH.toFloat(), 40f)
        scoresTable.y = SCREEN_HEIGHT - scoresTable.height

        labelCoins = Label("", Assets.labelStyleLarge)
        labelDistance = Label("", Assets.labelStyleLarge)
        labelBullets = Label("", Assets.labelStyleLarge)

        scoresTable.add(Image(TextureRegionDrawable(Assets.coin))).left().padLeft(5f)
        scoresTable.add(labelCoins).left()

        scoresTable.add(labelDistance).center().expandX()

        scoresTable.add(Image(TextureRegionDrawable(Assets.gun))).height(45f).width(30f).left()
        scoresTable.add(labelBullets).left().padRight(5f)

        buttonPause = Button(Assets.buttonPause)
        buttonPause.setSize(35f, 35f)
        buttonPause.setPosition((SCREEN_WIDTH - 40).toFloat(), (SCREEN_HEIGHT - 80).toFloat())
        addPressEffect(buttonPause)
        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPaused()
            }
        })

        stage!!.addActor(scoresTable)
        stage!!.addActor(buttonPause)
    }

    override fun update(delta: Float) {
        when (state) {
            STATE_RUNNING -> updateRunning(delta)
            STATE_GAME_OVER -> updateGameOver(delta)
        }
    }

    private fun updateRunning(delta: Float) {
        var accelerationX: Float

        accelerationX = -(Gdx.input.accelerometerX / 3f)

        if (Gdx.input.isKeyPressed(Input.Keys.A)) accelerationX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) accelerationX = 1f

        oWorld.update(delta, accelerationX, didFire, touchPositionWorldCoordinates)

        labelCoins.setText("x" + oWorld.coins)
        labelDistance.setText("Score " + oWorld.maxDistance)
        labelBullets.setText("x" + Settings.numBullets)

        if (oWorld.state == WorldGame.STATE_GAMEOVER) {
            setGameover()
        }

        didFire = false
    }

    private fun updateGameOver(delta: Float) {
        oWorld.update(delta, 0f, false, touchPositionWorldCoordinates)
    }

    override fun draw(delta: Float) {
        batch!!.begin()
        batch!!.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch!!.end()

        if (state != STATE_PAUSED) {
            renderer.render()
        }
    }

    private fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            pauseWindow.show(stage!!)
        }
    }

    fun setRunning() {
        state = STATE_RUNNING
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        Settings.setBestScore(oWorld.maxDistance)
        BaseWindowGameover(this).show(stage!!)
    }

    override fun hide() {
        super.hide()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchPositionWorldCoordinates[screenX.toFloat(), 0f] = 0f // Always as if I had touched the top of the screen
        renderer.unprojectToWorldCoordinates(touchPositionWorldCoordinates)

        didFire = true
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (pauseWindow.isVisible) pauseWindow.hide()
            else setPaused()
            return true
        }
        return super.keyDown(keycode)
    }

    companion object {
        const val STATE_RUNNING: Int = 2
        const val STATE_PAUSED: Int = 3
        const val STATE_GAME_OVER: Int = 4

    }
}
