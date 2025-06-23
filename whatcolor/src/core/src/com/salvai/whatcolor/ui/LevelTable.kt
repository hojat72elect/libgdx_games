package com.salvai.whatcolor.ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.global.EDIT_LEVELS
import com.salvai.whatcolor.global.PATTERN_SIZE
import com.salvai.whatcolor.global.SCREEN_WIDTH

class LevelTable(val game: WhatColor) : Table(game.skin) {

    val col = 3
    private val space = SCREEN_WIDTH * 0.05f
    private val patternSize = SCREEN_WIDTH * 0.26f

    init {
        defaults().space(space)
    }

    fun setUp() {
        clear()
        for (i in 0 until PATTERN_SIZE step col) {
            for (j in i until i + col)
                addPattern(j)
            row()
            for (j in i until i + col)
                setUpHighscoreLabel(j)
            row()
        }
    }

    private fun addPattern(i: Int) {
        val pattern = game.patterns.get(i)

        if (game.highscores[i] == -1 && !EDIT_LEVELS)
            pattern.lock()

        add(PatternTable(pattern).apply {

            this.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    if (game.highscores[i] > -1 && game.gameState == GameState.LEVEL_CHOOSE) {
                        game.gameFlowManager.currentLevel = i
                        game.gameFlowManager.showPattern(pattern, false)
                    }
                }
            })
        }).size(patternSize)
    }

    private fun setUpHighscoreLabel(i: Int) {
        add(Label(if (game.highscores[i] > -1) game.highscores[i].toString() else "", skin).apply { this.setAlignment(Align.center) }).spaceBottom(space * 2f)
    }
}