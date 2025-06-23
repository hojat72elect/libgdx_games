package com.salvai.whatcolor.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Array
import com.salvai.whatcolor.enums.PatternType

class Pattern(val singlePatternParts: Array<SinglePatternPart>, var patternType: PatternType) {

    init {
        singlePatternParts.random().secret = true
    }

    fun hideColor() {
        for (singlePattern in singlePatternParts)
            if (singlePattern.secret)
                singlePattern.addAction(Actions.fadeOut(0.4f, Interpolation.circle))
    }

    fun lock() {
        for (singlePattern in singlePatternParts)
            singlePattern.color = Color.DARK_GRAY
    }

    fun unlock() {
        for (singlePattern in singlePatternParts)
            singlePattern.color = singlePattern.colorz
    }

    fun showColor() {
        for (s in singlePatternParts) {
            s.clearActions()
            if (s.secret)
                s.addAction(Actions.fadeIn(0.4f, Interpolation.circle))
        }
    }


    fun getRandomVisible(): SinglePatternPart {
        val visiblePatternParts: Array<SinglePatternPart> = Array()
        for (s in singlePatternParts)
            if (!s.secret)
                visiblePatternParts.add(s)
        return visiblePatternParts.random()
    }

    fun randomize() {
        showColor()
        var secretIndex = -1
        for ((i, s) in singlePatternParts.withIndex())
            if (s.secret) {
                secretIndex = i
                s.secret = false
            }

        singlePatternParts.filterIndexed { index, _ -> index != secretIndex }.random().secret = true
    }

    fun getSecret(): SinglePatternPart? {
        for (s in singlePatternParts)
            if (s.secret)
                return s.copy().apply { color = colorz }
        return null
    }

    fun copy(): Pattern {
        val singlePatternPartsCopy = Array<SinglePatternPart>()
        for (s in singlePatternParts)
            singlePatternPartsCopy.add(s.copy())
        return Pattern(singlePatternPartsCopy, patternType)
    }
}