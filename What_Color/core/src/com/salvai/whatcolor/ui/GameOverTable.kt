package com.salvai.whatcolor.ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.global.BUTTON_HEIGHT
import com.salvai.whatcolor.global.BUTTON_SPACE
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.PATTERN_SIZE
import com.salvai.whatcolor.global.POINTS_TO_UNLOCK_NEXT_LEVEL
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import com.salvai.whatcolor.global.SECTION_SIZE

class GameOverTable(val game: WhatColor) : Table() {


    init {
        isTransform = true
        setBounds((SCREEN_WIDTH - PATTERN_SCREEN_SIZE) * 0.5f + SCREEN_WIDTH, SCREEN_HEIGHT * .1f, PATTERN_SCREEN_SIZE, SCREEN_HEIGHT * .8f)
        setOrigin(width * 0.5f, height * 0.5f)
        defaults().growX().space(BUTTON_SPACE).height(BUTTON_HEIGHT)
    }

    fun setUp() {
        clear()
        val scoreLabel: Label = Label("${game.gameFlowManager.score}", game.skin, "default").apply {
            setAlignment(Align.center)
            setFontScale(2f)
        }
        var text = if (game.gameFlowManager.currentLevel < PATTERN_SIZE && game.highscores[game.gameFlowManager.currentLevel + 1] == -1)
            if (game.highscores[game.gameFlowManager.currentLevel] > POINTS_TO_UNLOCK_NEXT_LEVEL)
                game.words["unlockedtext"]
            else
                game.words["unlocktext"]
        else
            game.words["newbesttext"]
        val textLabel: Label = Label(text, game.skin, "default").apply { setAlignment(Align.center) }


        val nextButton = TextButton("NEXT", game.skin, "default").apply {
            this.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    if (game.gameState == GameState.GAME_OVER) {
                        game.gameFlowManager.currentLevel = game.gameFlowManager.currentLevel + 1
                        game.gameFlowManager.showPattern(game.patterns[game.gameFlowManager.currentLevel], true)
                    }
                }
            })
        }

        val replayButton = TextButton("REPLAY", game.skin, "default").apply {
            this.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    if (game.gameState == GameState.GAME_OVER)
                        game.gameFlowManager.showPattern(game.gameFlowManager.pattern, true)
                }
            })
        }

        val menuButton = TextButton("MENU", game.skin, "default").apply {
            this.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    if (game.gameState == GameState.GAME_OVER)
                        game.gameFlowManager.showMenu()
                }
            })
        }

        add(textLabel).spaceBottom(100f)
        row()
        add(scoreLabel).spaceBottom(100f)
        row()
        if (game.highscores[game.gameFlowManager.currentLevel] >= POINTS_TO_UNLOCK_NEXT_LEVEL && game.gameFlowManager.currentLevel < PATTERN_SIZE * SECTION_SIZE) {
            add(nextButton)
            row()
        }
        add(replayButton)
        row()
        add(menuButton)
    }
}