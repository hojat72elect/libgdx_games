package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.Settings
import com.nopalsoft.ninjarunner.Settings.setNewScore
import com.nopalsoft.ninjarunner.leaderboard.NextGoalFrame
import com.nopalsoft.ninjarunner.leaderboard.Person
import com.nopalsoft.ninjarunner.scene2d.GameUI
import com.nopalsoft.ninjarunner.scene2d.MenuUI
import com.nopalsoft.ninjarunner.screens.Screens

class GameScreen(game: Game?, showMainMenu: Boolean) : Screens(game) {
    var gameWorld = GameWorld()
    var state: Int = 0
    var gameUI = GameUI(this, gameWorld)
    var menuUI = MenuUI(this, gameWorld)
    var renderer = WorldGameRenderer(batch!!, gameWorld)

    var nextGoalFrame: NextGoalFrame? = null

    init {

        if (showMainMenu) {
            state = STATE_MENU
            menuUI.show(stage!!, true)
        } else {
            setRunning(false)
        }
    }

    fun setRunning(removeMenu: Boolean) {
        val runAfterHideMenu = Runnable {
            val run = Runnable {
                state = STATE_RUNNING
                if (Settings.isMusicEnabled) {
                    Assets.music1!!.play()
                }

                nextGoalFrame = NextGoalFrame(SCREEN_WIDTH.toFloat(), 400f)
                setNextGoalFrame(0)
            }
            gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)))
            gameUI.show(stage!!)
        }

        if (removeMenu) {
            menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)))
            menuUI.removeWithAnimations()
        } else {
            stage?.addAction(Actions.run(runAfterHideMenu))
        }
    }

    override fun update(delta: Float) {
        if (state == STATE_MENU) {
            gameWorld.player?.updateStateTime(delta)
            gameWorld.mascot?.updateStateTime(delta)
        } else if (state == STATE_RUNNING) {
            gameWorld.update(delta, gameUI.didJump, gameUI.didDash, gameUI.didSlide)

            gameUI.didJump = false
            gameUI.didDash = false

            if (gameWorld.state == GameWorld.STATE_GAMEOVER) {
                setGameover()
            }

            setNextGoalFrame(gameWorld.scores)
        } else if (state == STATE_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                game.setScreen(GameScreen(game, true))
            }
        }
    }


    fun setNextGoalFrame(score: Long) {
        // So that only people you haven't passed yet are shown
        var score = score
        if (score < Settings.bestScore) score = Settings.bestScore

        game.arrayPerson.sort() // Arrange from highest score to lowest score


        var tempPerson: Person? = null
        // I calculate the position of the player with the most points. For example, if I'm in fifth place, this should be the position for fourth place.
        var nextGoalIndex = game.arrayPerson.size - 1
        // The arrangement is ordered from largest to smallest.
        while (nextGoalIndex >= 0) {
            val obj = game.arrayPerson.get(nextGoalIndex)
            if (obj.isMe) {
                nextGoalIndex--
                continue
            }

            if (obj.score > score) {
                tempPerson = obj
                break
            }
            nextGoalIndex--
        }

        val person = tempPerson

        if (person == null) return

        if (person == nextGoalFrame!!.person) return


        val run = Runnable {
            nextGoalFrame!!.updatePersona(person)
            nextGoalFrame!!.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH - NextGoalFrame.WIDTH, nextGoalFrame!!.getY(), 1f)))
        }

        if (!nextGoalFrame!!.hasParent()) {
            stage?.addActor(nextGoalFrame)
            Gdx.app.postRunnable(run)
        } else if (!nextGoalFrame!!.hasActions()) {
            nextGoalFrame!!.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH.toFloat(), nextGoalFrame!!.getY(), 1f), Actions.run(run)))
        }
    }

    private fun setGameover() {
        setNewScore(gameWorld.scores)
        state = STATE_GAME_OVER
        Assets.music1!!.stop()
    }

    override fun right() {
        super.right()
        gameUI.didDash = true
    }

    override fun draw(delta: Float) {
        if (state == STATE_MENU) {
            Assets.cloudsParallaxBackground!!.render(0f)
        } else {
            Assets.cloudsParallaxBackground!!.render(delta)
        }

        renderer.render(delta)

        camera.update()
        batch?.setProjectionMatrix(camera.combined)

        batch?.begin()
        Assets.smallFont!!.draw(batch, "FPS GERA" + Gdx.graphics.framesPerSecond, 5f, 20f)
        Assets.smallFont!!.draw(batch, "Bodies " + gameWorld.world.bodyCount, 5f, 40f)
        Assets.smallFont!!.draw(batch, "Lives " + gameWorld.player!!.lives, 5f, 60f)
        Assets.smallFont!!.draw(batch, "Coins " + gameWorld.coinsTaken, 5f, 80f)
        Assets.smallFont!!.draw(batch, "Scores " + gameWorld.scores, 5f, 100f)
        Assets.smallFont!!.draw(batch, "Distance " + gameWorld.player!!.position.x, 5f, 120f)
        Assets.smallFont!!.draw(batch, "Platforms " + gameWorld.arrayPlatform.size, 5f, 140f)

        batch?.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.R) {
            game.setScreen(GameScreen(game, true))
            return true
        } else if (keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            gameUI.didJump = true
            return true
        } else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            gameUI.didSlide = true
            return true
        } else if (keycode == Input.Keys.BACK) {
            Gdx.app.exit()
            return true
        } else if (keycode == Input.Keys.P) {
            if (game.arrayPerson != null) {
                setNextGoalFrame(0)
            }
            return true
        }
        return super.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            gameUI.didSlide = false
            return true
        }
        return super.keyUp(keycode)
    }

    override fun hide() {
        // nothing to do here
    }

    companion object {
        const val STATE_MENU: Int = 0
        const val STATE_RUNNING: Int = 1
        const val STATE_GAME_OVER: Int = 2
    }
}
