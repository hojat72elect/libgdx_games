package com.salvai.whatcolor.ui

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.salvai.whatcolor.actors.Pattern
import com.salvai.whatcolor.enums.PatternTableState
import com.salvai.whatcolor.global.MENU_ANIMATION_TIME
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE
import com.salvai.whatcolor.global.ROTATION
import com.salvai.whatcolor.global.SCREEN_HEIGHT
import com.salvai.whatcolor.global.SCREEN_WIDTH
import com.salvai.whatcolor.global.TABLE_ANIMATION_TIME
import kotlin.random.Random


class PatternTable(pattern: Pattern) : Table() {

    var patternState = PatternTableState.STILL

    init {
        isTransform = true
        setSize(PATTERN_SCREEN_SIZE, PATTERN_SCREEN_SIZE)
        setPosition(SCREEN_WIDTH * 0.5f - this.width * 0.5f, SCREEN_HEIGHT - this.height * 1.5f)
        setOrigin(width * 0.5f, height * 0.5f)
        fillTable(pattern)
    }


    fun updatePattern(pattern: Pattern) {
        clear()
        pattern.hideColor()
        fillTable(pattern)
        val left = Random.nextBoolean()
        patternState = PatternTableState.ANIMATED

        val changePatternState = RunnableAction()
        changePatternState.runnable = Runnable { patternState = PatternTableState.STILL }

        addAction(Actions.sequence(Actions.rotateBy(if (left) ROTATION else -ROTATION, TABLE_ANIMATION_TIME, Interpolation.circle), changePatternState))
        for (singlePattern in pattern.singlePatternParts)
            singlePattern.addAction(Actions.rotateBy(if (left) -ROTATION else ROTATION, TABLE_ANIMATION_TIME, Interpolation.circle))
    }


    private fun fillTable(pattern: Pattern) {
        for ((i, singlePatternPart) in pattern.singlePatternParts.withIndex()) {
            add(singlePatternPart).grow()
            if ((i + 1) % pattern.patternType.factor == 0)
                row()
        }
    }

    fun show() {
        addAction(Actions.sequence(Actions.scaleBy(-1f, -1f), Actions.delay(0.5f), Actions.scaleBy(1f, 1f, MENU_ANIMATION_TIME, Interpolation.circle)))
    }

    fun hide() {
        addAction(Actions.scaleBy(-1f, -1f, MENU_ANIMATION_TIME, Interpolation.circle))
    }
}
