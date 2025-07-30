package com.salvai.whatcolor.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.enums.GameState
import com.salvai.whatcolor.enums.PatternTableState
import com.salvai.whatcolor.global.MENU_ANIMATION_TIME
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.SCREEN_WIDTH
import java.util.Random

class Chooser(val game: WhatColor) : Table(game.skin) {

    val leftButton = Button(game.skin, "chooser")
    val rightButton = Button(game.skin, "chooser")
    private val size = PATTERN_SCREEN_SIZE * 0.5f
    var leftIsCorrect = false

    init {

        setSize(PATTERN_SCREEN_SIZE, PATTERN_SCREEN_SIZE)
        setPosition((SCREEN_WIDTH - PATTERN_SCREEN_SIZE) * 0.5f, -height)

        leftButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (game.gameState == GameState.PLAY_RUNNING && game.gameFlowManager.patternTable.patternState == PatternTableState.STILL)
                    if (leftIsCorrect)
                        game.gameFlowManager.nextPattern()
                    else
                        game.gameFlowManager.gameOver()
            }
        })

        rightButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (game.gameState == GameState.PLAY_RUNNING && game.gameFlowManager.patternTable.patternState == PatternTableState.STILL)
                    if (!leftIsCorrect)
                        game.gameFlowManager.nextPattern()
                    else
                        game.gameFlowManager.gameOver()
            }
        })

        add(leftButton).size(size)
        add(rightButton).size(size)
    }

    fun setColors(correctColor: Color?, wrongColor: Color?) {
        if (Random().nextBoolean())
            leftIsCorrectColor(correctColor, wrongColor)
        else
            rightIsCorrectColor(correctColor, wrongColor)
    }

    private fun leftIsCorrectColor(correctColor: Color?, wrongColor: Color?) {
        leftButton.color = correctColor
        rightButton.color = wrongColor
        leftIsCorrect = true
    }

    private fun rightIsCorrectColor(correctColor: Color?, wrongColor: Color?) {
        rightButton.color = correctColor
        leftButton.color = wrongColor
        leftIsCorrect = false
    }

    fun show() {
        addAction(Actions.moveBy(0f, height, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun hide() {
        addAction(Actions.moveBy(0f, -height, MENU_ANIMATION_TIME, Interpolation.circle))
    }


}