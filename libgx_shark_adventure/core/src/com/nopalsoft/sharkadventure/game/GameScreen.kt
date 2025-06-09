package com.nopalsoft.sharkadventure.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.sharkadventure.Achievements
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.Settings.setBestScore
import com.nopalsoft.sharkadventure.SharkAdventureGame
import com.nopalsoft.sharkadventure.scene2d.GameUI
import com.nopalsoft.sharkadventure.scene2d.MenuUI
import com.nopalsoft.sharkadventure.scene2d.PauseWindow
import com.nopalsoft.sharkadventure.screens.Screens

class GameScreen(game: SharkAdventureGame, showMainMenu: Boolean) : Screens(game) {

    var state = 0

    var gameWorld = GameWorld()
    var renderer = WorldRenderer(batch, gameWorld)

    var gameUI = GameUI(this, gameWorld)
    var menuUI = MenuUI(this, gameWorld)
    var pauseWindow = PauseWindow(this)

    var score = 0L

    /**
     * @param showMainMenu Shows the main menu otherwise starts the game immediately.
     */
    init {

        Assets.reloadBackground()

        if (showMainMenu) {
            state = STATE_MENU
            menuUI.show(stage, true)
        } else {
            setRunning(false)
        }

        Achievements.tryAgainAchievements()
    }

    override fun update(delta: Float) {
        when (state) {
            STATE_RUNNING -> updateRunning(delta)
            STATE_MENU -> updateStateMenu(delta)
        }
    }

    private fun updateRunning(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) gameUI.accelerationX = -1
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) gameUI.accelerationX = 1

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) gameUI.didSwimUp = true

        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.F)) gameUI.didFire = true

        gameWorld.update(delta, gameUI.accelerationX.toFloat(), gameUI.didSwimUp, gameUI.didFire)

        score = gameWorld.score.toLong()

        gameUI.lifeBar.updateActualNum(gameWorld.oShark.life.toFloat())
        gameUI.energyBar.updateActualNum(gameWorld.oShark.energy)

        gameUI.didSwimUp = false
        gameUI.didFire = false

        if (gameWorld.state == GameWorld.STATE_GAME_OVER) {
            setGameOver()
        }
    }

    private fun updateStateMenu(delta: Float) {
        gameWorld.oShark.updateStateTime(delta)
    }

    fun setRunning(removeMenu: Boolean) {
        val runAfterHideMenu = Runnable {
            val run = Runnable { state = STATE_RUNNING }
            gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)))
            gameUI.show(stage)
        }

        if (removeMenu) {
            menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)))
            menuUI.removeWithAnimations()
        } else {
            stage.addAction(Actions.run(runAfterHideMenu))
        }
    }

    private fun setGameOver() {
        if (state != STATE_GAME_OVER) {
            state = STATE_GAME_OVER
            val runAfterHideGameUI = Runnable { menuUI.show(stage, false) }

            setBestScore(score)

            gameUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideGameUI)))
            gameUI.removeWithAnimations()

            Settings.numberOfTimesPlayed++
        }
    }

    fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            gameUI.removeWithAnimations()
            pauseWindow.show(stage)
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.A || keycode == Input.Keys.D) gameUI.accelerationX = 0
        return super.keyUp(keycode)
    }

    override fun draw(delta: Float) {
        var delta = delta
        if (state == STATE_PAUSED || state == STATE_GAME_OVER) delta = 0f

        renderer.render(delta)

        camera.update()
        batch.setProjectionMatrix(camera.combined)
        batch.enableBlending()
        batch.begin()

        batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            if (state == STATE_RUNNING) {
                setPaused()
            } else if (state == STATE_PAUSED) {
                pauseWindow.hide()
                setRunning(false)
            } else if (state == STATE_MENU) {
                Gdx.app.exit()
            }
            return true
        }


        if (keycode == Input.Keys.P) {
            game.setScreen(GameScreen(game, false))
            return true
        }
        return super.keyDown(keycode)
    }

    companion object {
        private const val STATE_MENU = 0 // Main Menu
        private const val STATE_RUNNING = 1 // The game begins
        private const val STATE_PAUSED = 2 // Pause
        private const val STATE_GAME_OVER = 3 // Same as the main menu but without the title.
    }
}
