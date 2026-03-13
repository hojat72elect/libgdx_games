package com.salvai.whatcolor.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.actors.Pattern
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.global.BONUS
import com.salvai.whatcolor.global.PATTERN_SIZE
import com.salvai.whatcolor.global.POINTS_TO_UNLOCK_NEXT_LEVEL
import com.salvai.whatcolor.global.SHOW_TIME
import com.salvai.whatcolor.global.TIME
import com.salvai.whatcolor.screens.GameScreen
import com.salvai.whatcolor.ui.PatternTable


class GameFlowManager(val game: WhatColor, val gameScreen: GameScreen) {


    lateinit var patternTable: PatternTable
    lateinit var pattern: Pattern
    var time = TIME
    var score = 0
    var isGameOver = false
    var currentLevel = 0


    fun showPattern(selectedPattern: Pattern, replay: Boolean) {
        game.gameState = GameState.PLAY_SHOW_PATTERN
        resetValues()
        pattern = selectedPattern.copy()
        pattern.randomize()
        patternTable = PatternTable(pattern)
        game.stage.addActor(patternTable)
        patternTable.show()
        gameScreen.timeBarAndScoreLabelTable.show()
        if (replay)
            gameScreen.hideGameOverTable()
        else
            gameScreen.hideLevelScrollPane(false)
    }

    private fun resetValues() {
        isGameOver = false
        game.gameState = GameState.PLAY_SHOW_PATTERN
        time = 1f
        score = 0
        gameScreen.timeBarAndScoreLabelTable.scoreLabel.setText("$score")
    }

    fun startGame() {
        game.gameState = GameState.PLAY_RUNNING
        pattern.hideColor()
        gameScreen.chooser.show()
        gameScreen.chooser.setColors(pattern.getSecret()?.colorz, pattern.getRandomVisible().colorz)

        game.stage.addActor(gameScreen.chooser)

    }

    fun update(delta: Float) {
        if (!isGameOver) {
            if (game.gameState == GameState.PLAY_SHOW_PATTERN) {
                time += delta * SHOW_TIME
                if (time >= TIME)
                    startGame()
            } else
                time -= delta
            gameScreen.timeBarAndScoreLabelTable.timeBar.value = time
            if (time <= 0)
                gameOver()
        }
    }

    fun gameOver() {
        if (game.vibrate)
            Gdx.input.vibrate(200)
        isGameOver = true
        game.gameState = GameState.GAME_OVER
        saveResult()
        gameScreen.gameOverTable.setUp()
        pattern.showColor()
        patternTable.hide()
        gameScreen.timeBarAndScoreLabelTable.hide()
        gameScreen.chooser.hide()
        gameScreen.showGameOverTable()
    }

    private fun saveResult() {
        if (score > game.highscores[game.gameFlowManager.currentLevel])
            game.highscores[game.gameFlowManager.currentLevel] = score

        if (score >= POINTS_TO_UNLOCK_NEXT_LEVEL)
            unlockNextLevel()
        game.savePreferences()
    }

    private fun unlockNextLevel() {
        if (game.gameFlowManager.currentLevel < PATTERN_SIZE && game.highscores[game.gameFlowManager.currentLevel + 1] == -1) {
            game.patterns.get(game.gameFlowManager.currentLevel + 1).unlock()
            game.highscores[game.gameFlowManager.currentLevel + 1] = 0
        }
    }

    fun showMenu() {
        gameScreen.hideGameOverTable()
        gameScreen.showMenu(true)
    }

    fun showLevelTable() {
        game.gameState = GameState.LEVEL_CHOOSE
        gameScreen.hideLogo()
        gameScreen.showLevelTable()
    }

    fun nextPattern() {
        score = score.inc()

        gameScreen.timeBarAndScoreLabelTable.scoreContainer.addAction(
            Actions.sequence(
                Actions.scaleBy(0.5f, 0.5f, 0.1f, Interpolation.circle),
                Actions.run { gameScreen.timeBarAndScoreLabelTable.scoreLabel.setText("" + score) },
                Actions.scaleBy(-0.5f, -0.5f, 0.1f, Interpolation.circle)
            )
        )

        if (time + BONUS <= TIME)
            time += BONUS
        else
            time = TIME

        if (game.vibrate)
            Gdx.input.vibrate(100)

        pattern.randomize()
        patternTable.updatePattern(pattern)
        gameScreen.chooser.setColors(pattern.getSecret()?.colorz, pattern.getRandomVisible().colorz)

    }


}