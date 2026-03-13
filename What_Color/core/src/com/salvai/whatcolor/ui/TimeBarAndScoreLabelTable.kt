package com.salvai.whatcolor.ui

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.salvai.whatcolor.WhatColor
import com.salvai.whatcolor.global.MENU_ANIMATION_TIME
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import kotlin.math.roundToInt

class TimeBarAndScoreLabelTable(game: WhatColor) : Table(game.skin) {

    val timeBar = TimeBar((SCREEN_WIDTH * 0.6).roundToInt(), 40, game.skin)

    val scoreLabel: Label = Label("0", game.skin, "default").apply {
        this.setAlignment(Align.center)
        setFontScale(2f)
    }
    var scoreContainer = Container<Label>(scoreLabel).apply {
        isTransform = true
        setSize(PATTERN_SCREEN_SIZE, 50f)
        setOrigin(width * 0.5f, height * 0.5f)
    }

    init {
        isTransform = true

        defaults().width(PATTERN_SCREEN_SIZE).space(30f)

        setBounds((SCREEN_WIDTH - PATTERN_SCREEN_SIZE) * 0.5f, SCREEN_HEIGHT, PATTERN_SCREEN_SIZE, SCREEN_HEIGHT * .2f)
        setOrigin(width * 0.5f, height * 0.5f)

        add(timeBar)
        row()
        add(scoreContainer)
    }

    fun show() {
        addAction(Actions.moveBy(0f, -height, MENU_ANIMATION_TIME, Interpolation.circle))
    }

    fun hide() {
        addAction(Actions.moveBy(0f, height, MENU_ANIMATION_TIME, Interpolation.circle))
    }

}