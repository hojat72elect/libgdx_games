package com.nopalsoft.lander.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Peripheral
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.Assets.cargarMapa
import com.nopalsoft.lander.MainLander
import com.nopalsoft.lander.Settings.setStarsFromLevel
import com.nopalsoft.lander.dialogs.VentanaCompleted
import com.nopalsoft.lander.dialogs.VentanaGameOver
import com.nopalsoft.lander.dialogs.VentanaPaused
import com.nopalsoft.lander.game.WorldGame.Companion.STATE_NEXT_LEVEL
import com.nopalsoft.lander.game.objetos.LifeBar
import com.nopalsoft.lander.screens.MainMenuScreen
import com.nopalsoft.lander.screens.Screens

class GameScreen(game: MainLander, val level: Int) : Screens(game) {
    var oWorld: WorldGame
    var renderer: WorldGameRenderer

    var sensibilidad: Float = 3f

    var lifeBar: LifeBar
    var gasBar: LifeBar
    var marcoStats: Table

    var btPause: ImageButton
    var dialogGameover: VentanaGameOver
    var dialogPaused: VentanaPaused
    var dialogNextLevel: VentanaCompleted

    init {
        cargarMapa(level)
        oWorld = WorldGame()
        renderer = WorldGameRenderer(batcher, oWorld)

        dialogGameover = VentanaGameOver(game, oWorld, level)
        dialogPaused = VentanaPaused(game, oWorld, level)
        dialogNextLevel = VentanaCompleted(game, oWorld, level)

        // Marcador Stats
        marcoStats = Table()
        marcoStats.setSize(172f, 98f)
        marcoStats.setBackground(Assets.marcoStats)
        marcoStats.setPosition(0f, (SCREEN_HEIGHT - 99).toFloat())

        lifeBar = LifeBar(oWorld.oNave!!.vida)
        gasBar = LifeBar(oWorld.oNave!!.gas)

        marcoStats.add<LifeBar?>(lifeBar).width(90f).height(25f).padLeft(35f).padBottom(5f)
        marcoStats.row()
        marcoStats.add<LifeBar?>(gasBar).width(90f).height(25f).padLeft(35f).padTop(6f)

        // Pause Button
        btPause = ImageButton(Assets.styleImageButtonPause)
        btPause.setSize(32f, 32f)
        btPause.setPosition(SCREEN_WIDTH - btPause.getWidth() - 5, SCREEN_HEIGHT - btPause.getHeight() - 5)
        btPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                setPaused()
            }
        })

        stage.addActor(marcoStats)
        stage.addActor(btPause)

        state = STATE_RUNNING
    }

    override fun update(delta: Float) {
        when (state) {
            STATE_READY -> {}
            STATE_RUNNING -> updateRunning(delta)
        }
    }

    private fun updateRunning(delta: Float) {
        var accelX = 0f
        var accelY = 0f

        if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
            accelX = Gdx.input.accelerometerX / sensibilidad * -1
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) accelX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) accelX = 1f

        if (Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) accelY = 1f

        oWorld.update(delta, accelY, accelX)

        lifeBar.updateActualLife(oWorld.oNave!!.vida)
        gasBar.updateActualLife(oWorld.oNave!!.gas)

        if (oWorld.state == WorldGame.Companion.STATE_GAME_OVER) {
            setGameover()
        } else if (oWorld.state == STATE_NEXT_LEVEL) {
            setNextLevel()
        }
    }

    override fun draw(delta: Float) {
        renderer.render()
        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)

        batcher.begin()
        batcher.end()
    }

    private fun setPaused() {
        state = STATE_PAUSED
        dialogPaused.show(stage)
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        dialogGameover.show(stage)
    }

    private fun setNextLevel() {
        state = STATE_GAME_OVER
        setStarsFromLevel(level, oWorld.estrellasTomadas)
        dialogNextLevel.show(stage)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            game.setScreen(MainMenuScreen(game))
            return true
        }
        return false
    }

    companion object {
        const val STATE_READY: Int = 0
        const val STATE_RUNNING: Int = 1
        const val STATE_PAUSED: Int = 2
        const val STATE_GAME_OVER: Int = 3
        var state: Int = 0
    }
}
