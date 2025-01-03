package com.nopalsoft.sharkadventure.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.sharkadventure.Achievements
import com.nopalsoft.sharkadventure.Assets.reloadBackground
import com.nopalsoft.sharkadventure.MainShark
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.Settings.changeBestScore
import com.nopalsoft.sharkadventure.scene2d.GameUI
import com.nopalsoft.sharkadventure.scene2d.MenuUI
import com.nopalsoft.sharkadventure.scene2d.PauseWindowGroup
import com.nopalsoft.sharkadventure.screens.BaseScreen

/**
 * @param showMainMenu Show main menu otherwise start game immediately.
 */
class GameScreen(game: MainShark, showMainMenu: Boolean) : BaseScreen(game) {



    var punctuation = 0L
    var state = 0
    private var worldGame = WorldGame()
    private var renderer = WorldRenderer(batcher, worldGame)
    private var gameUI = GameUI(this, worldGame)
    private var menuUI = MenuUI(this, worldGame)
    private var pauseWindow = PauseWindowGroup(this)


    init {
        reloadBackground()

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
        if (Gdx.input.isKeyPressed(Input.Keys.A)) gameUI.speedX = -1
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) gameUI.speedX = 1

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) gameUI.didSwimUp = true

        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.F)) gameUI.didFire = true

        worldGame.update(delta, gameUI.speedX.toFloat(), gameUI.didSwimUp, gameUI.didFire)

        punctuation = worldGame.score.toLong()

        gameUI.lifeBar.updateActualNum(worldGame.shark.life.toFloat())
        gameUI.energyBar.updateActualNum(worldGame.shark.energy)

        gameUI.didSwimUp = false
        gameUI.didFire = false

        if (worldGame.state == WorldGame.STATE_GAME_OVER) {
            setGameOver()
        }
    }

    private fun updateStateMenu(delta: Float) {
        worldGame.shark.updateStateTime(delta)
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

            changeBestScore(punctuation)

            gameUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideGameUI)))
            gameUI.removeWithAnimations()


            Settings.numberOfTimesPlayed++
            if (Settings.numberOfTimesPlayed % 4f == 0f) {
                game.reqHandler.showInterstitial()
            }
            game.reqHandler.showAdBanner()
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
        if (keycode == Input.Keys.A || keycode == Input.Keys.D) gameUI.speedX = 0
        return super.keyUp(keycode)
    }

    override fun draw(delta: Float) {
        var delta = delta
        if (state == STATE_PAUSED || state == STATE_GAME_OVER) delta = 0f

        renderer.render(delta)

        camera.update()
        batcher.projectionMatrix = camera.combined
        batcher.enableBlending()
        batcher.begin()

        batcher.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if ((keycode == Input.Keys.BACK) or (keycode == Input.Keys.ESCAPE)) {
            when (state) {
                STATE_RUNNING -> {
                    setPaused()
                }

                STATE_PAUSED -> {
                    pauseWindow.hide()
                    setRunning(false)
                }

                STATE_MENU -> {
                    Gdx.app.exit()
                }
            }
            return true
        }
        if (keycode == Input.Keys.L) {
            game.facebookHandler.facebookSignIn()
            return true
        }

        if (keycode == Input.Keys.P) {
            game.screen = GameScreen(game, false)
            return true
        }
        return super.keyDown(keycode)
    }

    companion object {
        private const val STATE_MENU = 0 // User is in the main menu
        private const val STATE_RUNNING = 1 // The game is running
        private const val STATE_PAUSED = 2 // The game is paused
        private const val STATE_GAME_OVER = 3 // Game is over. Same as the main menu but without the title.
    }
}
