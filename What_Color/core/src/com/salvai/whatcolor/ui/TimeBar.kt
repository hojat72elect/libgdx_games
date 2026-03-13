package com.salvai.whatcolor.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.salvai.whatcolor.global.MENU_ANIMATION_TIME
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import com.salvai.whatcolor.global.TIME
import com.salvai.whatcolor.global.getColoredDrawable

class TimeBar(width: Int, height: Int, skin: Skin) : ProgressBar(0f, TIME, 0.01f, false, skin) {
    init {
        style.background = getColoredDrawable(width, height, Color.DARK_GRAY)
        style.knob = getColoredDrawable(0, height, Color.WHITE)
        style.knobBefore = getColoredDrawable(width, height, Color.WHITE)
        setBounds((SCREEN_WIDTH - PATTERN_SCREEN_SIZE) * 0.5f, SCREEN_HEIGHT * 0.95f, PATTERN_SCREEN_SIZE, SCREEN_WIDTH * 0.005f)
        setAnimateDuration(0.05f)
        setAnimateInterpolation(Interpolation.circle)
        value = 0f
    }

    fun show() {
        addAction(Actions.sequence(Actions.scaleBy(-1f, -1f), Actions.delay(0.5f), Actions.scaleBy(1f, 1f, MENU_ANIMATION_TIME, Interpolation.circle)))
    }

    fun hide() {
        addAction(Actions.scaleBy(-1f, -1f, MENU_ANIMATION_TIME, Interpolation.circle))
    }
}